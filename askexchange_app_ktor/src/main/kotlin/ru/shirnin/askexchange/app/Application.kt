package ru.shirnin.askexchange.app

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.shirnin.askexchange.app.plugins.*
import ru.shirnin.askexchange.business.InnerAnswerProcessor
import ru.shirnin.askexchange.business.InnerQuestionProcessor
import ru.shirnin.askexchange.inner.models.InnerChainSettings
import ru.shirnin.askexchange.logging.common.LoggerProvider
import ru.shirnin.askexchange.logging.logback.loggerLogback
import ru.shirnin.askexchange.repo.`in`.memory.AnswerRepoInMemory
import ru.shirnin.askexchange.repo.`in`.memory.QuestionRepoInMemory
import ru.shirnin.askexchange.repo.postgre.PostgreAnswerRepo
import ru.shirnin.askexchange.repo.postgre.PostgreQuestionRepo
import ru.shirnin.askexchange.repo.postgre.SqlProperties
import ru.shirnin.askexchange.repo.stubs.AnswerRepositoryStub
import ru.shirnin.askexchange.repo.stubs.QuestionRepositoryStub

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(appSettings: AskAppSettings = initAppSettings()) {
    configureSerialization()
    configureHTTP(appSettings)
    configureMonitoring(appSettings)
    configureSockets()
    //configureSecurity()
    configureRouting(appSettings)
}

fun Application.initAppSettings(): AskAppSettings {
    val sqlUrl = System.getenv("SQL_URL") ?: "jdbc:postgresql://localhost:5432/askexchange_postgre_db"
    val props = SqlProperties(
        url = sqlUrl
    )

    val chainSettings = InnerChainSettings(
        loggerProvider = getLoggerProviderConf(),
        questionRepoTest = QuestionRepoInMemory(),
        questionRepoStub = QuestionRepoInMemory(),
        questionRepoProd = PostgreQuestionRepo(properties = props),
        answerRepoTest = AnswerRepoInMemory(),
        answerRepoStub = AnswerRepoInMemory(),
        answerRepoProd = PostgreAnswerRepo(properties = props)
    )

    return AskAppSettings(
        appUrls = listOf(
            "http://127.0.0.1:5432/",
            "http://127.0.0.1:8080/",
            "http://0.0.0.0:8080/",
            "http://192.168.0.182:8080/"
        ),//environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        chainSettings = chainSettings,
        questionProcessor = InnerQuestionProcessor(),
        answerProcessor = InnerAnswerProcessor()
    )
}

fun Application.initIntegrationTestAppSettings(dbUrl: String = "jdbc:postgresql://localhost:5432/askexchange_postgre_db",
                                               dbUser: String = "postgres",
                                               dbPassword: String = "postgres",
                                               dbSchema: String = "askexchange",
                                               dbDropDatabase: Boolean = false,
                                               dbFastMigration: Boolean = true): AskAppSettings {
    val chainSettings = InnerChainSettings(
        loggerProvider = getLoggerProviderConf(),
        questionRepoTest = QuestionRepoInMemory(),
        questionRepoStub = QuestionRepoInMemory(),
        questionRepoProd = PostgreQuestionRepo(
            properties = SqlProperties(
                url = dbUrl,
                user = dbUser,
                password = dbPassword,
                schema = dbSchema,
                dropDatabase = dbDropDatabase,
                fastMigration = dbFastMigration
            )
        ),
        answerRepoTest = AnswerRepoInMemory(),
        answerRepoStub = AnswerRepoInMemory(),
        answerRepoProd = PostgreAnswerRepo(
            properties = SqlProperties(
                url = dbUrl,
                user = dbUser,
                password = dbPassword,
                schema = dbSchema,
                dropDatabase = dbDropDatabase,
                fastMigration = dbFastMigration
            )
        ),
    )

    return AskAppSettings(
        appUrls = listOf(
            "http://127.0.0.1:5432/",
            "http://127.0.0.1:8080/",
            "http://0.0.0.0:8080/",
            "http://192.168.0.182:8080/"
        ),//environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        chainSettings = chainSettings,
        questionProcessor = InnerQuestionProcessor(),
        answerProcessor = InnerAnswerProcessor()
    )
}

fun Application.initTestAppSettings(): AskAppSettings {
    val chainSettings = InnerChainSettings(
        loggerProvider = getLoggerProviderConf(),
        questionRepoTest = QuestionRepoInMemory(),
        questionRepoStub = QuestionRepoInMemory(),
        questionRepoProd = QuestionRepositoryStub(),
        answerRepoTest = AnswerRepoInMemory(),
        answerRepoStub = AnswerRepoInMemory(),
        answerRepoProd = AnswerRepositoryStub()
    )

    return AskAppSettings(
        appUrls = listOf(
            "http://127.0.0.1:8080/",
            "http://0.0.0.0:8080/",
            "http://192.168.0.182:8080/"
        ),//environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        chainSettings = chainSettings,
        questionProcessor = InnerQuestionProcessor(),
        answerProcessor = InnerAnswerProcessor()
    )
}

fun Application.getLoggerProviderConf(): LoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> LoggerProvider { loggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Allowed value is logback only")
    }
