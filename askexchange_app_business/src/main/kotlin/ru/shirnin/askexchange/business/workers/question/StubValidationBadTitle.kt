package ru.shirnin.askexchange.business.workers.question

import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

fun MultipleCommandBuilder<InnerQuestionContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    isContextHealthy { stubCase == InnerStubs.BAD_TITLE && state == InnerState.RUNNING }
    handle {
        state = InnerState.FAILED
        this.errors.add(
            InnerError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}
