package ru.shirnin.askexchange.business.workers.answer

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import InnerAnswerStub
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs


fun MultipleCommandBuilder<InnerAnswerContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    isContextHealthy { stubCase == InnerStubs.SUCCESS && state == InnerState.RUNNING }
    handle {
        state = InnerState.FINISHED
        val stub = InnerAnswerStub.prepareResult {
            answerRequest.body.takeIf { it.isNotBlank() }?.also { this.body = it }
            answerRequest.likes.also { this.likes = it }
        }
        answerResponse = stub
    }
}