package ru.shirnin.askexchange.chain.dsl

import ru.shirnin.askexchange.chain.Command
import ru.shirnin.askexchange.chain.CommandWorker

@ChainDslMarker
class CommandWorkerBuilder<T> : AbstractCommandBuilder<T>(), HandleCommandBuilder<T> {
    private var handler: suspend T.() -> Unit = {}
    override fun handle(function: suspend T.() -> Unit) {
        handler = function
    }

    override fun build(): Command<T> = CommandWorker(
        title = title,
        description = description,
        contextChecker = contextChecker,
        handler = handler,
        exceptionHandler = exceptionHandler
    )

}