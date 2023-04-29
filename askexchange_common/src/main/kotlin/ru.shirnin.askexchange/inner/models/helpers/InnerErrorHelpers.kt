package ru.shirnin.askexchange.inner.models.helpers

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerState

fun Throwable.asInnerError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = InnerError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun InnerQuestionContext.addError(vararg error: InnerError) = errors.addAll(error)

fun InnerQuestionContext.fail(error: InnerError) {
    addError(error)
    state = InnerState.FAILED
}

fun InnerAnswerContext.addError(vararg error: InnerError) = errors.addAll(error)

fun InnerAnswerContext.fail(error: InnerError) {
    addError(error)
    state = InnerState.FAILED
}

fun errorValidation(
    field: String,
    /**
     * Code describing an error. Shouldn't include field name or mention validation.
     * Examples of correct use: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: InnerError.Level = InnerError.Level.ERROR,
) = InnerError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Code describing an error. Shouldn't include field name or mention validation.
     * Examples of correct use: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    level: InnerError.Level = InnerError.Level.ERROR,
) = InnerError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)