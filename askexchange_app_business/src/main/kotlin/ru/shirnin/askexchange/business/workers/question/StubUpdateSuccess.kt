package ru.shirnin.askexchange.business.workers.question

import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import InnerQuestionStub
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs


fun MultipleCommandBuilder<InnerQuestionContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    isContextHealthy { stubCase == InnerStubs.SUCCESS && state == InnerState.RUNNING }
    handle {
        state = InnerState.FINISHED
        val stub = InnerQuestionStub.prepareResult {
            questionRequest.id.takeIf { it != ru.shirnin.askexchange.inner.models.InnerId.NONE }?.also { this.id = it }
            questionRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            questionRequest.body.takeIf { it.isNotBlank() }?.also { this.body = it }
        }
        questionResponse = stub
    }
}
