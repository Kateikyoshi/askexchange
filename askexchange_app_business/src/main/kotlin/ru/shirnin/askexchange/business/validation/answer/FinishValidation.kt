package ru.shirnin.askexchange.business.validation.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState

fun MultipleCommandBuilder<InnerAnswerContext>.finishAnswerValidation(title: String) = worker {
    this.title = title
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        answerValidated = answerValidating
    }
}