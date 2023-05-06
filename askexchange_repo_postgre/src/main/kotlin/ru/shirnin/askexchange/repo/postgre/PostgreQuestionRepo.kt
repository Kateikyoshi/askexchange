package ru.shirnin.askexchange.repo.postgre

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.postgre.tables.AnswerTable
import ru.shirnin.askexchange.repo.postgre.tables.QuestionTable
//import ru.shirnin.askexchange.repo.postgre.tables.UserTable
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.DbQuestionResponse
import ru.shirnin.askexchange.repo.question.QuestionRepository
import java.lang.IllegalArgumentException

class PostgreQuestionRepo(
    properties: SqlProperties,
    initObjects: Collection<InnerQuestion> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() }
) : QuestionRepository {

    private val driver = when {
        properties.url.contains("postgre") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Nothing but postgre is supported")
    }

    private val connect = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    init {
        transaction {
            SchemaUtils.create(QuestionTable, AnswerTable)
            //SchemaUtils.create(UserTable, QuestionTable, AnswerTable)
        }
    }

    //TODO: We lost an ID. Gotta get it somehow
    override suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse =
        transaction {
            val response = QuestionTable.insert {
                it[id] = randomUuid()
                it[title] = request.question.title
                it[body] = request.question.body
                //it[user] = request.question.username
                it[lock] = randomUuid()
            }

            DbQuestionResponse(data = QuestionTable.from(response), isSuccess = true)
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

}