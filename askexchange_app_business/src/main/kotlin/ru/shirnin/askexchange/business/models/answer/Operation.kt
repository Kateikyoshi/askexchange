package ru.shirnin.askexchange.business.models.answer

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain


fun MultipleCommandBuilder<InnerAnswerContext>.operation(
    title: String, command: InnerCommand,
    functions: MultipleCommandBuilder<InnerAnswerContext>.() -> Unit
) = chain {
    functions()
    this.title = title
    isContextHealthy { this.command == command && state == InnerState.RUNNING }
}