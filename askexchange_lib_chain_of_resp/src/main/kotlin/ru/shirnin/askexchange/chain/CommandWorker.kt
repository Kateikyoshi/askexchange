package ru.shirnin.askexchange.chain

class CommandWorker<T>(
    private val handler: suspend T.() -> Unit = {},
    title: String,
    description: String = "",
    contextChecker: suspend T.() -> Boolean = { true },
    exceptionHandler: suspend T.(Throwable) -> Unit = {},
) : AbstractCommand<T>(title, description, contextChecker, exceptionHandler) {
    override suspend fun handle(context: T) = handler(context)
}
