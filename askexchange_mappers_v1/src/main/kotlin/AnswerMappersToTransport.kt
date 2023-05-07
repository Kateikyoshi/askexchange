import exceptions.UnknownInnerCmd
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.inner.models.InnerAnswerContext

fun InnerAnswerContext.toTransport(): IAnswerResponse = when (val cmd = command) {
    InnerCommand.CREATE -> toTransportCreate()
    InnerCommand.DELETE -> toTransportDelete()
    InnerCommand.UPDATE -> toTransportUpdate()
    InnerCommand.READ -> toTransportRead()
    InnerCommand.NONE -> throw UnknownInnerCmd(cmd)
}

fun InnerAnswerContext.toTransportCreate() = AnswerCreateResponse(
    responseType = "CREATE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answerId = answerResponse.id.asString(),
    versionLock = answerResponse.lock.asString()
)

fun InnerAnswerContext.toTransportDelete() = AnswerDeleteResponse(
    responseType = "DELETE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answerId = answerResponse.id.asString(),
    versionLock = answerResponse.lock.asString()
)

fun InnerAnswerContext.toTransportUpdate() = AnswerUpdateResponse(
    responseType = "UPDATE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answerId = answerResponse.id.asString(),
    versionLock = answerResponse.lock.asString()
)

fun InnerAnswerContext.toTransportRead() = AnswerReadResponse(
    responseType = "READ",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.FINISHED) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answer = answerResponse.toTransport(),
    versionLock = answerResponse.lock.asString()
)

fun InnerAnswer.toTransport() = Answer(
    body = body,
    date = date.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
    likes = likes.toInt()
)

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