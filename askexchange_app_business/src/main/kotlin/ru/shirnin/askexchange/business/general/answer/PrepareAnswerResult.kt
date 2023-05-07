package ru.shirnin.askexchange.business.general.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.InnerWorkMode

fun MultipleCommandBuilder<InnerAnswerContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Preparing data to be send back to client"
    isContextHealthy { workMode != InnerWorkMode.STUB }
    handle {
        answerResponse = answerRepoDone
        state = when (val st = state) {
            InnerState.RUNNING -> InnerState.FINISHED
            else -> st
        }
    }
}