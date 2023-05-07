package ru.shirnin.askexchange.business.workers.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

fun MultipleCommandBuilder<InnerQuestionContext>.stubDbError(title: String) = worker {
    this.title = title
    isContextHealthy { stubCase == InnerStubs.DB_ERROR && state == InnerState.RUNNING }
    handle {
        state = InnerState.FAILED
        this.errors.add(
            InnerError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}