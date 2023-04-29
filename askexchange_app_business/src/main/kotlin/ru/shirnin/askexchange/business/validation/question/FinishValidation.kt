package ru.shirnin.askexchange.business.validation.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState

fun MultipleCommandBuilder<InnerQuestionContext>.finishQuestionValidation(title: String) = worker {
    this.title = title
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        questionValidated = questionValidating
    }
}