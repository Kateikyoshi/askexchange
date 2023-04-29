package ru.shirnin.askexchange.business.models.question


import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.InnerWorkMode
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain

fun MultipleCommandBuilder<InnerQuestionContext>.stubs(
    title: String,
    functions: MultipleCommandBuilder<InnerQuestionContext>.() -> Unit
) = chain {
    functions()
    this.title = title
    isContextHealthy { workMode == InnerWorkMode.STUB && state == InnerState.RUNNING }
}
