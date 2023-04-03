package ru.shirnin.askexchange.chain.dsl

/**
 * DSL builder for chains of commands
 */
@ChainDslMarker
interface MultipleCommandBuilder<T> : CommandBuilder<T> {
    fun add(worker: CommandBuilder<T>)
}