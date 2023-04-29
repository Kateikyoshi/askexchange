package ru.shirnin.askexchange.app

import ru.shirnin.askexchange.logging.common.LoggerProvider

data class AskAppChainSettings(
    val loggerProvider: LoggerProvider = LoggerProvider()
) {
    companion object {
        @Suppress("unused")
        val NONE = AskAppChainSettings()
    }
}