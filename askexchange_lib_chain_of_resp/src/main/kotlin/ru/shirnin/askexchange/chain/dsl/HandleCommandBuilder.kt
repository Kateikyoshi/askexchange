package ru.shirnin.askexchange.chain.dsl

/**
 * Builder for a Worker
 */
@ChainDslMarker
interface HandleCommandBuilder<T> : CommandBuilder<T> {
    fun handle(function: suspend T.() -> Unit)
}