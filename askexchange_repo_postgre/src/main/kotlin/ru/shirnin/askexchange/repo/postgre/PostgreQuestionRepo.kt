package ru.shirnin.askexchange.repo.postgre

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.helpers.errorRepoConcurrency
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.postgre.tables.AnswerTable
import ru.shirnin.askexchange.repo.postgre.tables.QuestionTable
import ru.shirnin.askexchange.repo.postgre.tables.UserTable
//import ru.shirnin.askexchange.repo.postgre.tables.UserTable
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.DbQuestionResponse
import ru.shirnin.askexchange.repo.question.QuestionRepository
import java.lang.IllegalArgumentException
import java.sql.SQLException

class PostgreQuestionRepo(
    properties: SqlProperties,
    initObjects: Collection<InnerQuestion> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() }
) : QuestionRepository {

    private val db by lazy { SqlConnector(properties).connect(QuestionTable, AnswerTable, UserTable) }

    init {
        initObjects.forEach {
            save(it)
        }
    }

    /**
     * User is supposed to exist prior to Question creation.
     * But he doesn't exist in this half-**** implementation, so he is fictionally inserted.
     * Otherwise, Question table won't be able to reference that
     * parentUserId and transaction will fail.
     */
    private fun save(question: InnerQuestion): DbQuestionResponse {
        return safeTransaction({

            //***atrocity part start
            val insertedParentUserId = UserTable.insertIgnore {
                if (question.parentUserId != InnerId.NONE) {
                    it[id] = question.parentUserId.asString()
                }
            } get UserTable.id
            //***atrocity part end

            val response = QuestionTable.insert {
                if (question.id != InnerId.NONE) {
                    it[id] = question.id.asString()
                }
                it[title] = question.title
                it[body] = question.body
                it[parentUserId] = insertedParentUserId
                it[lock] = question.lock.asString()
            }

            DbQuestionResponse(QuestionTable.from(response), true)
        }, {
            DbQuestionResponse(
                data = null,
                isSuccess = false,
                errors = listOf(InnerError(message = message ?: localizedMessage))
            )
        })
    }

    override suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse {
        val question = request.question.copy(
            lock = InnerVersionLock(randomUuid()),
            id = request.question.id.takeIf { it != InnerId.NONE } ?: InnerId(
                randomUuid()
            )
        )
        return save(question)
    }


    override suspend fun readQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        return safeTransaction({
            println("before read: ${request.id.asString()}")
            //StarWarsFilms.slice(StarWarsFilms.name.countDistinct())
            val count = QuestionTable.slice(QuestionTable.id.countDistinct())
            println("before read count: $count")
//            val result =
//                (QuestionTable innerJoin UserTable).select { QuestionTable.id eq request.id.asString() }.single()
            val result = QuestionTable.select { QuestionTable.id eq request.id.asString() }.single()

            DbQuestionResponse(QuestionTable.from(result), true)
        }, {
            val error = when (this) {
                is NoSuchElementException -> InnerError(field = "id", message = "Not Found 6", code = notFoundCode)
                is IllegalArgumentException -> InnerError(message = "More than one element with the same id")
                else -> InnerError(message = localizedMessage)
            }
            DbQuestionResponse(data = null, isSuccess = false, errors = listOf(error))
        })
    }

    override suspend fun updateQuestion(request: DbQuestionRequest): DbQuestionResponse {
        val key = request.question.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.question.lock.takeIf { it != InnerVersionLock.NONE }?.asString()
        val newQuestion = request.question.copy(lock = InnerVersionLock(randomUuid()))

        return safeTransaction({
            val local = QuestionTable.select { QuestionTable.id eq key }.singleOrNull()?.let {
                QuestionTable.from(it)
            } ?: return@safeTransaction resultErrorNotFound

            return@safeTransaction when (oldLock) {
                null, local.lock.asString() -> updateDb(newQuestion)
                else -> resultErrorConcurrent(local.lock.asString(), local)
            }
        }, {
            DbQuestionResponse(
                data = request.question,
                isSuccess = false,
                errors = listOf(InnerError(field = "id", message = "Not Found 9", code = notFoundCode))
            )

        })
    }

    private fun updateDb(newQuestion: InnerQuestion): DbQuestionResponse {

        //***atrocity part start
        UserTable.insertIgnore {
            if (newQuestion.parentUserId != InnerId.NONE) {
                it[id] = newQuestion.parentUserId.asString()
            }
        }
        //***atrocity part end

        QuestionTable.update({ QuestionTable.id eq newQuestion.id.asString() }) {
            it[title] = newQuestion.title
            it[body] = newQuestion.body
            it[parentUserId] = newQuestion.parentUserId.asString()
            it[lock] = newQuestion.lock.asString()
        }
        val result = QuestionTable.select { QuestionTable.id eq newQuestion.id.asString() }.single()

        return DbQuestionResponse(data = QuestionTable.from(result), isSuccess = true)
    }

    override suspend fun deleteQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        val key = request.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId

        return safeTransaction({
            val local = QuestionTable.select { QuestionTable.id eq key }.single().let { QuestionTable.from(it) }
            if (local.lock == request.lock) {
                QuestionTable.deleteWhere { id eq request.id.asString() }
                DbQuestionResponse(data = local, isSuccess = true)
            } else {
                resultErrorConcurrent(request.lock.asString(), local)
            }
        }, {
            DbQuestionResponse(
                data = null,
                isSuccess = false,
                errors = listOf(InnerError(field = "id", message = "Not Found: ${this.message}"))
            )
        })
    }

    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    companion object {
        private const val notFoundCode = "not-found 000"

        val resultErrorEmptyId = DbQuestionResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                InnerError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )

        fun resultErrorConcurrent(lock: String, question: InnerQuestion?) = DbQuestionResponse(
            data = question,
            isSuccess = false,
            errors = listOf(
                errorRepoConcurrency(InnerVersionLock(lock), question?.lock?.let { InnerVersionLock(it.asString()) })
            )
        )

        val resultErrorNotFound = DbQuestionResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                InnerError(
                    field = "id",
                    message = "Not Found kk",
                    code = notFoundCode
                )
            )
        )
    }
}