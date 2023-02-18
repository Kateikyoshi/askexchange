import exceptions.UnknownInnerCmd
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import models.InnerCommand
import models.InnerError
import models.InnerState
import models.answer.InnerAnswer
import ru.shirnin.askexchange.api.v1.models.*

fun InnerAnswerContext.toTransport(): IAnswerResponse = when (val cmd = command) {
    InnerCommand.CREATE -> toTransportCreate()
    InnerCommand.DELETE -> toTransportDelete()
    InnerCommand.UPDATE -> toTransportUpdate()
    InnerCommand.READ -> toTransportRead()
    InnerCommand.NONE -> throw UnknownInnerCmd(cmd)
}

private fun InnerAnswerContext.toTransportCreate() = AnswerCreateResponse(
    responseType = "CREATE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answerId = answerResponse.id.asString()
)

private fun InnerAnswerContext.toTransportDelete() = AnswerDeleteResponse(
    responseType = "DELETE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answerId = answerResponse.id.asString()
)

private fun InnerAnswerContext.toTransportUpdate() = AnswerUpdateResponse(
    responseType = "UPDATE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answerId = answerResponse.id.asString()
)

private fun InnerAnswerContext.toTransportRead() = AnswerReadResponse(
    responseType = "READ",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answer = answerResponse.toTransport()
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
    message = message.takeIf { it.isNotBlank() }
)