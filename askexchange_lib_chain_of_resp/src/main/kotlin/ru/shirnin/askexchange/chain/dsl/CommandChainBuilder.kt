package ru.shirnin.askexchange.chain.dsl

import ru.shirnin.askexchange.chain.Command
import ru.shirnin.askexchange.chain.CommandChain
import ru.shirnin.askexchange.chain.executeSequential

@ChainDslMarker
class CommandChainBuilder<T>(
    private val handler: suspend (T, List<Command<T>>) -> Unit = ::executeSequential,
) : AbstractCommandBuilder<T>(), MultipleCommandBuilder<T> {
    private val workers: MutableList<CommandBuilder<T>> = mutableListOf()
    override fun add(worker: CommandBuilder<T>) {
        workers.add(worker)
    }

    override fun build(): Command<T> = CommandChain(
        title = title,
        description = description,
        commands = workers.map { it.build() },
        handler = handler,
        contextChecker = contextChecker,
        exceptionHandler = exceptionHandler
    )
}