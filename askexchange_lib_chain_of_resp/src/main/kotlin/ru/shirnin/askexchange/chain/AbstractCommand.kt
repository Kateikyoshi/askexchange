package ru.shirnin.askexchange.chain

abstract class AbstractCommand<T>(
    override val title: String,
    override val description: String = "",
    private val contextChecker: suspend T.() -> Boolean = { true }, //good by default
    private val exceptionHandler: suspend T.(Throwable) -> Unit = {},
): Command<T> {
    protected abstract suspend fun handle(context: T)

    private suspend fun isContextHealthy(context: T): Boolean = context.contextChecker()
    private suspend fun handleException(context: T, e: Throwable) = context.exceptionHandler(e)

    override suspend fun execute(context: T) {
        if (isContextHealthy(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                handleException(context, e)
            }
        }
    }
}
