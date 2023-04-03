package ru.shirnin.askexchange.logging.common

import kotlin.reflect.KClass

class LoggerProvider(
    private val provider: (String) -> AskLogWrapper = { AskLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)

    @Suppress("unused")
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")
}