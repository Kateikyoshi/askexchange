package ru.shirnin.askexchange.repo.postgre

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.answer.AnswerRepository
import ru.shirnin.askexchange.repo.answer.DbAnswerIdRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerResponse
import ru.shirnin.askexchange.repo.postgre.tables.AnswerTable
import ru.shirnin.askexchange.repo.postgre.tables.QuestionTable
//import ru.shirnin.askexchange.repo.postgre.tables.UserTable
import java.lang.IllegalArgumentException

class PostgreAnswerRepo(
    properties: SqlProperties,
    initObjects: Collection<InnerQuestion> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() }
) : AnswerRepository {

    private val driver = when {
        properties.url.contains("postgre") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Nothing but postgre is supported")
    }

    private val connect = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    init {
        transaction {
            //SchemaUtils.create(UserTable, QuestionTable, AnswerTable)
            SchemaUtils.create(QuestionTable, AnswerTable)
        }
    }

    override suspend fun createAnswer(request: DbAnswerRequest): DbAnswerResponse =
        transaction {
            val response = AnswerTable.insert {
                it[id] = request.answer.id.asString()
                it[body] = request.answer.body
                it[date] = request.answer.date.toString()
                it[likes] = request.answer.likes.toInt()
                it[lock] = randomUuid()
            }

            DbAnswerResponse(data = AnswerTable.from(response), isSuccess = true)
        }


    override suspend fun readAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateAnswer(request: DbAnswerRequest): DbAnswerResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        TODO("Not yet implemented")
    }


}