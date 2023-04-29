package ru.shirnin.askexchange.business.general

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.InnerWorkMode

fun MultipleCommandBuilder<InnerQuestionContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    isContextHealthy { workMode != InnerWorkMode.STUB}
    handle {
        questionResponse = questionRepoDone
        state = when (val st = state) {
            InnerState.RUNNING -> InnerState.FINISHED
            else -> st
        }
    }
}