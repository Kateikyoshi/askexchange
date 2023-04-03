package ru.shirnin.askexchange.app.plugins

import io.ktor.server.plugins.callloging.*
import org.slf4j.event.*
import io.ktor.server.application.*
import ru.shirnin.askexchange.app.AskAppSettings
import ru.shirnin.askexchange.app.module
import ru.shirnin.askexchange.logging.logback.LogWrapperLogback

private val clazz = Application::module::class.qualifiedName ?: "Application"

fun Application.configureMonitoring(appSettings: AskAppSettings) {
    install(CallLogging) {
        level = Level.INFO

        val logWrapper = appSettings.chainSettings.loggerProvider
            .logger(clazz) as? LogWrapperLogback

        val wrappedLogger = logWrapper?.logger
        if (wrappedLogger != null) logger = wrappedLogger
    }
}
