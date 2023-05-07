package ru.shirnin.askexchange.repo.postgre

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.postgre.tables.AnswerTable
import ru.shirnin.askexchange.repo.postgre.tables.QuestionTable
import ru.shirnin.askexchange.repo.postgre.tables.UserTable
//import ru.shirnin.askexchange.repo.postgre.tables.UserTable
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.DbQuestionResponse
import ru.shirnin.askexchange.repo.question.QuestionRepository
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
        TODO("Not yet implemented")
    }

    override suspend fun updateQuestion(request: DbQuestionRequest): DbQuestionResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        TODO("Not yet implemented")
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

}