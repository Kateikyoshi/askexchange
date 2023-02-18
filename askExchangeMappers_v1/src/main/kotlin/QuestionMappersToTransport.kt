import exceptions.UnknownInnerCmd
import models.InnerCommand
import models.InnerError
import models.InnerState
import models.answer.InnerAnswer
import ru.shirnin.askexchange.api.v1.models.*

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
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    questionId = questionResponse.id.asString()
)

fun InnerQuestionContext.toTransportDelete() = QuestionDeleteResponse(
    responseType = "DELETE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    questionId = questionResponse.id.asString()
)

fun InnerQuestionContext.toTransportUpdate() = QuestionUpdateResponse(
    responseType = "UPDATE",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    questionId = questionResponse.id.asString()
)

fun InnerQuestionContext.toTransportRead() = QuestionReadResponse(
    responseType = "READ",
    debugId = this.debugId.asString().takeIf { it.isNotBlank() },
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    question = Question(),
    answers = answersOfQuestionResponse.toTransport()
)

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
    message = message.takeIf { it.isNotBlank() }
)