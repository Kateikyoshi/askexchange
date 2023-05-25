package ru.shirnin.askexchange.app.plugins

import io.ktor.server.application.*
import ru.shirnin.askexchange.app.conf.PostgresConfig
import ru.shirnin.askexchange.repo.answer.AnswerRepository
import ru.shirnin.askexchange.repo.`in`.memory.AnswerRepoInMemory
import ru.shirnin.askexchange.repo.`in`.memory.QuestionRepoInMemory
import ru.shirnin.askexchange.repo.postgre.PostgreAnswerRepo
import ru.shirnin.askexchange.repo.postgre.PostgreQuestionRepo
import ru.shirnin.askexchange.repo.postgre.SqlProperties
import ru.shirnin.askexchange.repo.question.QuestionRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.getQuestionDatabaseConf(type: InnerDbType): QuestionRepository {
    val dbSettingPath = "marketplace.repository.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()

    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initQuestionInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initQuestionPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of the following values: 'in-memory' or 'postgres'"
        )
    }
}

fun Application.getAnswerDatabaseConf(type: InnerDbType): AnswerRepository {
    val dbSettingPath = "marketplace.repository.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()

    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initAnswerInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initAnswerPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of the following values: 'in-memory' or 'postgres'"
        )
    }
}

private fun Application.initQuestionPostgres(): QuestionRepository {
    val config = PostgresConfig(environment.config)
    return PostgreQuestionRepo(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}

private fun Application.initAnswerPostgres(): AnswerRepository {
    val config = PostgresConfig(environment.config)
    return PostgreAnswerRepo(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}

private fun Application.initQuestionInMemory(): QuestionRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return QuestionRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

private fun Application.initAnswerInMemory(): AnswerRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return AnswerRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

enum class InnerDbType(val confName: String) {
    PROD("prod"), TEST("test")
}