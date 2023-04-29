package ru.shirnin.askexchange.business.validation.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.helpers.errorValidation
import ru.shirnin.askexchange.inner.models.helpers.fail

fun MultipleCommandBuilder<InnerQuestionContext>.validateBodyNotEmpty(title: String) = worker {
    this.title = title
    isContextHealthy { questionValidating.body.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "body",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
