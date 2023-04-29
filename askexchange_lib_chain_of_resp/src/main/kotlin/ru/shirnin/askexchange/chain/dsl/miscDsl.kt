package ru.shirnin.askexchange.chain.dsl

import ru.shirnin.askexchange.chain.executeParallel

/**
 * Entry point to a DSL for building chains of commands.
 * Commands are executed in sequential manner.
 *
 * Example of DSL:
 * ```
 *  chain<SomeContext> {
 *      worker {
 *      }
 *      chain {
 *          worker(...) {
 *          }
 *          worker(...) {
 *          }
 *      }
 *      parallel {
 *         ...
 *      }
 *  }
 * ```
 */
fun <T> rootChain(function: MultipleCommandBuilder<T>.() -> Unit): MultipleCommandBuilder<T> =
    CommandChainBuilder<T>().apply(function)


/**
 * Creates a sequentially executed chain of commands
 */
fun <T> MultipleCommandBuilder<T>.chain(function: MultipleCommandBuilder<T>.() -> Unit) {
    add(CommandChainBuilder<T>().apply(function))
}

/**
 * Creates a chain of commands which is executed in parallel.
 * Not very thread safe, any synchronization and blocks have to be manual
 */
fun <T> MultipleCommandBuilder<T>.parallel(function: MultipleCommandBuilder<T>.() -> Unit) {
    add(CommandChainBuilder<T>(::executeParallel).apply(function))
}

/**
 * Creates a worker
 */
fun <T> MultipleCommandBuilder<T>.worker(function: HandleCommandBuilder<T>.() -> Unit) {
    add(CommandWorkerBuilder<T>().apply(function))
}

/**
 * Creates a worker with default context checker and default exception handler
 */
fun <T> MultipleCommandBuilder<T>.worker(
    title: String,
    description: String = "",
    handler: T.() -> Unit
) {
    add(CommandWorkerBuilder<T>().also {
        it.title = title
        it.description = description
        it.handle(handler)
    })
}