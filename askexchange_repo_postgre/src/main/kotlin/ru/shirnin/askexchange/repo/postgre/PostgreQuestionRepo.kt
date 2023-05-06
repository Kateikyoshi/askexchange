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

    private fun save(item: InnerQuestion): DbQuestionResponse {
        return safeTransaction({

            val response = QuestionTable.insert {
                if (item.id != InnerId.NONE) {
                    it[id] = item.id.asString()
                }
                it[title] = item.title
                it[body] = item.body
                //it[user] = item.username
                it[lock] = item.lock.asString()
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
            id = request.question.id.takeIf { it != InnerId.NONE } ?: InnerId(randomUuid())
        )
        return save(question)
    }

    //override suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse =
    //        transaction {
    //            val response = QuestionTable.insert {
    //                it[id] = randomUuid()
    //                it[title] = request.question.title
    //                it[body] = request.question.body
    //                //it[user] = request.question.username
    //                it[lock] = randomUuid()
    //            }
    //
    //            DbQuestionResponse(data = QuestionTable.from(response), isSuccess = true)
    //        }


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