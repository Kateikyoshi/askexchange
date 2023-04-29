package ru.shirnin.askexchange.business.models.answer

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.InnerWorkMode
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain


fun MultipleCommandBuilder<InnerAnswerContext>.stubs(
    title: String,
    functions: MultipleCommandBuilder<InnerAnswerContext>.() -> Unit
) = chain {
    functions()
    this.title = title
    isContextHealthy { workMode == InnerWorkMode.STUB && state == InnerState.RUNNING }
}