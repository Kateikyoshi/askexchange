package ru.shirnin.askexchange.business.validation.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.helpers.errorValidation
import ru.shirnin.askexchange.inner.models.helpers.fail

fun MultipleCommandBuilder<InnerQuestionContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    isContextHealthy {
        questionValidating.lock != InnerVersionLock.NONE
                && !questionValidating.lock.asString().matches(regExp)
    }
    handle {
        val encodedId = questionValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}