package ru.shirnin.askexchange.repo.postgre

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.helpers.errorRepoConcurrency
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.answer.AnswerRepository
import ru.shirnin.askexchange.repo.answer.DbAnswerIdRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerResponse
import ru.shirnin.askexchange.repo.postgre.tables.AnswerTable
import ru.shirnin.askexchange.repo.postgre.tables.QuestionTable
import ru.shirnin.askexchange.repo.postgre.tables.UserTable
import ru.shirnin.askexchange.repo.question.DbQuestionResponse
import java.lang.IllegalArgumentException
//import ru.shirnin.askexchange.repo.postgre.tables.UserTable
import java.sql.SQLException

class PostgreAnswerRepo(
    properties: SqlProperties,
    initObjects: Collection<InnerAnswer> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() }
) : AnswerRepository {

    private val db by lazy { SqlConnector(properties).connect(QuestionTable, AnswerTable, UserTable) }

    init {
        initObjects.forEach {
            save(it)
        }
    }

    /**
     * Unlike with Question situation is better here since Question is supposed to
     * exist by this time. And if it exists, then User also exists
     */
    private fun save(answer: InnerAnswer): DbAnswerResponse {
        return safeTransaction({

            val response = AnswerTable.insert {
                if (answer.id != InnerId.NONE) {
                    it[id] = answer.id.asString()
                }
                it[body] = answer.body
                it[date] = answer.date.toString()
                it[likes] = answer.likes.toInt()
                it[parentUserId] = answer.parentUserId.asString()
                it[parentQuestionId] = answer.parentQuestionId.asString()
                it[lock] = answer.lock.asString()
            }

            DbAnswerResponse(AnswerTable.from(response), true)
        }, {
            DbAnswerResponse(
                data = null,
                isSuccess = false,
                errors = listOf(InnerError(message = message ?: localizedMessage))
            )
        })
    }

    override suspend fun createAnswer(request: DbAnswerRequest): DbAnswerResponse {
        val answer = request.answer.copy(
            lock = InnerVersionLock(randomUuid()),
            id = request.answer.id.takeIf { it != InnerId.NONE } ?: InnerId(
                randomUuid()
            )
        )
        return save(answer)
    }


    override suspend fun readAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        return safeTransaction({
            val result = (AnswerTable innerJoin UserTable innerJoin QuestionTable)
                .select { AnswerTable.id.eq(request.id.asString()) }.single()

            DbAnswerResponse(AnswerTable.from(result), true)
        }, {
            val error = when (this) {
                is NoSuchElementException -> InnerError(field = "id", message = "Not Found", code = notFoundCode)
                is IllegalArgumentException -> InnerError(message = "More than one element with the same id")
                else -> InnerError(message = localizedMessage)
            }
            DbAnswerResponse(data = null, isSuccess = false, errors = listOf(error))
        })
    }

    override suspend fun updateAnswer(request: DbAnswerRequest): DbAnswerResponse {
        val key = request.answer.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.answer.lock.takeIf { it != InnerVersionLock.NONE }?.asString()
        val newAnswer = request.answer.copy(lock = InnerVersionLock(randomUuid()))

        return safeTransaction({
            val local = AnswerTable.select { AnswerTable.id eq key }.singleOrNull()?.let {
                AnswerTable.from(it)
            } ?: return@safeTransaction resultErrorNotFound

            return@safeTransaction when (oldLock) {
                null, local.lock.asString() -> updateDb(newAnswer)
                else -> resultErrorConcurrent(local.lock.asString(), local)
            }
        }, {
            DbAnswerResponse(
                data = request.answer,
                isSuccess = false,
                errors = listOf(InnerError(field = "id", message = "Not Found 9", code = notFoundCode))
            )
        })
    }

    /**
     * In case if we have a new owner of an answer we may
     * need to insert his new id to User table too.
     * But Question id has to exist already, prepare for exception if it doesn't
     */
    private fun updateDb(newAnswer: InnerAnswer): DbAnswerResponse {

        //***atrocity part start
        UserTable.insertIgnore {
            if (newAnswer.parentUserId != InnerId.NONE) {
                it[id] = newAnswer.parentUserId.asString()
            }
        }
        //***atrocity part end

        AnswerTable.update({ AnswerTable.id eq newAnswer.id.asString() }) {
            it[body] = newAnswer.body
            it[parentUserId] = newAnswer.parentUserId.asString()
            it[parentQuestionId] = newAnswer.parentQuestionId.asString()
            it[lock] = newAnswer.lock.asString()
        }
        val result = AnswerTable.select { AnswerTable.id eq newAnswer.id.asString() }.single()

        return DbAnswerResponse(data = AnswerTable.from(result), isSuccess = true)
    }

    override suspend fun deleteAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        val key = request.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId

        return safeTransaction({
            val local = AnswerTable.select { AnswerTable.id eq key }.single().let { AnswerTable.from(it) }
            if (local.lock == request.lock) {
                AnswerTable.deleteWhere { id eq request.id.asString() }
                DbAnswerResponse(data = local, isSuccess = true)
            } else {
                resultErrorConcurrent(request.lock.asString(), local)
            }
        }, {
            DbAnswerResponse(
                data = null,
                isSuccess = false,
                errors = listOf(InnerError(field = "id", message = "Not Found"))
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

        private const val notFoundCode = "not-found"

        val resultErrorEmptyId = DbAnswerResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                InnerError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )

        fun resultErrorConcurrent(lock: String, answer: InnerAnswer?) = DbAnswerResponse(
            data = answer,
            isSuccess = false,
            errors = listOf(
                errorRepoConcurrency(InnerVersionLock(lock), answer?.lock?.let { InnerVersionLock(it.asString()) })
            )
        )

        val resultErrorNotFound = DbAnswerResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                InnerError(
                    field = "id",
                    message = "Not Found",
                    code = notFoundCode
                )
            )
        )
    }
}