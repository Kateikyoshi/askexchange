import exceptions.UnknownRequestClass
import kotlinx.datetime.Instant
import models.*
import models.answer.InnerAnswer
import models.question.InnerQuestion
import ru.shirnin.askexchange.api.v1.models.*
import stubs.InnerStubs

fun InnerAnswerContext.fromTransport(request: IAnswerRequest) = when (request) {
    is AnswerCreateRequest -> fromTransport(request)
    is AnswerDeleteRequest -> fromTransport(request)
    is AnswerUpdateRequest -> fromTransport(request)
    is AnswerReadRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun IAnswerRequest.obtainDebugId(): InnerDebugId {
    val debugId: String = if (this.debugId != null) this.debugId!! else ""
    return InnerDebugId(debugId)
}

private fun InnerAnswerContext.fromTransport(request: AnswerCreateRequest) {
    command = InnerCommand.CREATE
    question = request.answerCreateObject?.questionId?.toInnerQuestionOnlyById() ?: InnerQuestion()
    user = request.answerCreateObject?.userId?.toInnerUserOnlyById() ?: InnerUser()
    answerRequest = request.answerCreateObject?.answer?.toInner() ?: InnerAnswer()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerAnswerContext.fromTransport(request: AnswerDeleteRequest) {
    command = InnerCommand.DELETE
    answerRequest = request.answerDeleteObject?.answerId?.toInnerOnlyById() ?: InnerAnswer()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerAnswerContext.fromTransport(request: AnswerUpdateRequest) {
    command = InnerCommand.UPDATE
    answerRequest = request.toInnerWithId()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerAnswerContext.fromTransport(request: AnswerReadRequest) {
    command = InnerCommand.READ
    answerRequest = request.answerReadObject?.answerId?.toInnerOnlyById() ?: InnerAnswer()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun AnswerUpdateRequest.toInnerWithId() = InnerAnswer(
    id = InnerId(this.answerUpdateObject?.answerId ?: ""),
    body = this.answerUpdateObject?.answer?.body ?: "",
    likes = this.answerUpdateObject?.answer?.likes?.toLong() ?: 0
)

private fun Answer.toInner() = InnerAnswer(
    id = InnerId(""),
    body = this.body ?: "",
    date = if (date != null) Instant.parse(date!!) else Instant.DISTANT_PAST,
    likes = 0L
)

private fun String.toInnerUserOnlyById() = InnerUser(id = this.formInnerId())
private fun String.toInnerQuestionOnlyById() = InnerQuestion(id = this.formInnerId())
private fun String.formInnerId() = InnerId(this)
private fun String.toInnerOnlyById() = InnerAnswer(id = this.formInnerId())


