package ru.shirnin.askexchange.business.workers.question

import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.helpers.fail
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker

fun MultipleCommandBuilder<InnerQuestionContext>.stubNoCase(title: String) = worker {
    this.title = title
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        fail(
            InnerError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}

