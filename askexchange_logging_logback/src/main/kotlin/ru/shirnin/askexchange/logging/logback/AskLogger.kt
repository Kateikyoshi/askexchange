package ru.shirnin.askexchange.logging.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.shirnin.askexchange.logging.common.AskLogWrapper
import kotlin.reflect.KClass

fun loggerLogback(logger: Logger): AskLogWrapper = LogWrapperLogback(
    logger = logger,
    loggerId = logger.name
)


@Suppress("unused")
fun loggerLogback(clazz: KClass<*>): AskLogWrapper = loggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

fun loggerLogback(loggerId: String): AskLogWrapper = loggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
