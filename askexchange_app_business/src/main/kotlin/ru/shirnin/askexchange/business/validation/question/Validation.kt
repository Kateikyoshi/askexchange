package ru.shirnin.askexchange.business.validation.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState

fun MultipleCommandBuilder<InnerQuestionContext>.validation(block: MultipleCommandBuilder<InnerQuestionContext>.() -> Unit) = chain {
    block()
    title = "Validation"

    isContextHealthy { state == InnerState.RUNNING }
}
