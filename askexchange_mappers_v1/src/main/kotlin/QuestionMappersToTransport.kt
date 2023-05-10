import exceptions.UnknownInnerCmd
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.inner.models.*
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

fun InnerQuestionContext.toTransport(): IQuestionResponse = when (val cmd = command) {
    InnerCommand.CREATE -> toTransportCreate()
    InnerCommand.DELETE -> toTransportDelete()
    InnerCommand.UPDATE -> toTransportUpdate()
    InnerCommand.READ -> toTransportRead()
    InnerCommand.NONE -> throw UnknownInnerCmd(cmd)
}

fun InnerQuestionContext.toTransportCreate() = QuestionCreateResponse(
    responseType = "CREATE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    questionId = questionResponse.id.asString(),
    versionLock = questionResponse.lock.asString()
)

fun InnerQuestionContext.toTransportDelete() = QuestionDeleteResponse(
    responseType = "DELETE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    questionId = questionResponse.id.asString(),
    versionLock = questionResponse.lock.asString()
)

fun InnerQuestionContext.toTransportUpdate() = QuestionUpdateResponse(
    responseType = "UPDATE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    questionId = questionResponse.id.asString(),
    versionLock = questionResponse.lock.asString()
)

fun InnerQuestionContext.toTransportRead() = QuestionReadResponse(
    responseType = "READ",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    question = questionResponse.toTransport(),
    answers = answersOfQuestionResponse.toTransport(),
    versionLock = questionResponse.lock.asString()
)

private fun InnerQuestion.toTransport(): Question {
    return Question(
        title = this.title,
        body = this.body
    )
}

private fun List<InnerAnswer>.toTransport() = this.
    map { it.toTransport() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun List<InnerError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun InnerError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)