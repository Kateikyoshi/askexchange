package ru.shirnin.askexchange.app

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.shirnin.askexchange.app.plugins.*
import ru.shirnin.askexchange.business.InnerAnswerProcessor
import ru.shirnin.askexchange.business.InnerQuestionProcessor
import ru.shirnin.askexchange.logging.common.LoggerProvider
import ru.shirnin.askexchange.logging.logback.loggerLogback

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

fun Application.initAppSettings(): AskAppSettings = AskAppSettings(
    appUrls = listOf("http://127.0.0.1:8080/", "http://0.0.0.0:8080/", "http://192.168.0.182:8080/"),//environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    chainSettings = AskAppChainSettings(getLoggerProviderConf()),
    questionProcessor = InnerQuestionProcessor(),
    answerProcessor = InnerAnswerProcessor()
)

fun Application.getLoggerProviderConf(): LoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> LoggerProvider { loggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Allowed value is logback only")
    }
