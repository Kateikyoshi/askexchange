package ru.shirnin.askexchange.chain

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Реализация цепочки (chain), которая исполняет свои вложенные цепочки и рабочие
 * в соответствии со стратегией handler
 */
class CommandChain<T>(
    private val commands: List<Command<T>>,
    private val handler: suspend (T, List<Command<T>>) -> Unit,
    title: String,
    description: String = "",
    contextChecker: suspend T.() -> Boolean = { true },
    exceptionHandler: suspend T.(Throwable) -> Unit = {},
) : AbstractCommand<T>(title, description, contextChecker, exceptionHandler) {
    override suspend fun handle(context: T) = handler(context, commands)
}

/**
 * Sequential execution strategy for context
 */
suspend fun <T> executeSequential(context: T, commands: List<Command<T>>): Unit =
    commands.forEach {
        it.execute(context)
    }

/**
 * Parallel execution strategy for context
 */
suspend fun <T> executeParallel(context: T, commands: List<Command<T>>): Unit = coroutineScope {
    commands.forEach {
        launch { it.execute(context) }
    }
}
