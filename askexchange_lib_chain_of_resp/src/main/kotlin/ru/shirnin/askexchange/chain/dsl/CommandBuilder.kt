package ru.shirnin.askexchange.chain.dsl

import ru.shirnin.askexchange.chain.Command

/**
 * Base DSL builder for Command
 */
@ChainDslMarker
interface CommandBuilder<T> {
    var title: String
    var description: String
    fun isContextHealthy(function: suspend T.() -> Boolean)
    fun handleException(function: suspend T.(e: Throwable) -> Unit)

    fun build(): Command<T>
}

