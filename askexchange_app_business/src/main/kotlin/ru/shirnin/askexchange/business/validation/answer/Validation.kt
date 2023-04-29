package ru.shirnin.askexchange.business.validation.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState

fun MultipleCommandBuilder<InnerAnswerContext>.validation(block: MultipleCommandBuilder<InnerAnswerContext>.() -> Unit) = chain {
    block()
    title = "Validation"

    isContextHealthy { state == InnerState.RUNNING }
}
