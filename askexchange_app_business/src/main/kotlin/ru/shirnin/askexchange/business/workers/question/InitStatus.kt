package ru.shirnin.askexchange.business.workers.question

import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker

fun MultipleCommandBuilder<InnerQuestionContext>.initStatus(title: String) = worker {
    this.title = title
    isContextHealthy { state == InnerState.NONE }
    handle { state = InnerState.RUNNING }
}

