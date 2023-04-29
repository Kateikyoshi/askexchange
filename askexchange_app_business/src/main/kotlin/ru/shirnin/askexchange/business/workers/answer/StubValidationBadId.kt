package ru.shirnin.askexchange.business.workers.answer

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

fun MultipleCommandBuilder<InnerAnswerContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    isContextHealthy { stubCase == InnerStubs.BAD_ID && state == InnerState.RUNNING }
    handle {
        state = InnerState.FAILED
        this.errors.add(
            InnerError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}