package ru.shirnin.askexchange.chain.dsl

abstract class AbstractCommandBuilder<T> : CommandBuilder<T> {
    protected var contextChecker: suspend T.() -> Boolean = { true }
    protected var exceptionHandler: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun isContextHealthy(function: suspend T.() -> Boolean) {
        contextChecker = function
    }

    override fun handleException(function: suspend T.(e: Throwable) -> Unit) {
        exceptionHandler = function
    }
}