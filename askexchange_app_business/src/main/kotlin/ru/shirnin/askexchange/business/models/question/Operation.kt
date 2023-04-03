package ru.shirnin.askexchange.business.models.question

import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain

fun MultipleCommandBuilder<InnerQuestionContext>.operation(
    title: String, command: InnerCommand,
    functions: MultipleCommandBuilder<InnerQuestionContext>.() -> Unit
) = chain {
        functions()
        this.title = title
        isContextHealthy { this.command == command && state == InnerState.RUNNING }
    }


