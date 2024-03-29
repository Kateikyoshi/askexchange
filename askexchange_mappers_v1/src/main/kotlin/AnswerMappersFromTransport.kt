import exceptions.UnknownRequestClass
import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.inner.models.*
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

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
    answerRequest = request.toInnerWithParentUserAndQuestionId()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerAnswerContext.fromTransport(request: AnswerDeleteRequest) {
    command = InnerCommand.DELETE
    answerRequest = request.toInnerWithId()

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
    likes = this.answerUpdateObject?.answer?.likes?.toLong() ?: 0,
    lock = this.answerUpdateObject?.versionLock.toInnerVersionLock()
)

private fun AnswerDeleteRequest.toInnerWithId() = InnerAnswer(
    id = InnerId(this.answerDeleteObject?.answerId ?: ""),
    lock = this.answerDeleteObject?.versionLock.toInnerVersionLock()
)

private fun AnswerCreateRequest.toInnerWithParentUserAndQuestionId(): InnerAnswer {
    val innerAnswer = this.answerCreateObject?.answer?.toInner() ?: InnerAnswer()
    innerAnswer.parentUserId = this.answerCreateObject?.userId.formInnerId()
    innerAnswer.parentQuestionId = this.answerCreateObject?.questionId.formInnerId()
    return innerAnswer
}

private fun Answer.toInner() = InnerAnswer(
    id = InnerId(""),
    body = this.body ?: "",
    date = if (date != null) Instant.parse(date!!) else Instant.DISTANT_PAST,
    likes = this.likes?.toLong() ?: 0
)
private fun String?.toInnerQuestionOnlyById() = InnerQuestion(id = this.formInnerId())
private fun String?.formInnerId() = this?.let { InnerId(it) } ?: InnerId.NONE
private fun String?.toInnerOnlyById() = InnerAnswer(id = this.formInnerId())

private fun String?.toInnerVersionLock() = this?.let { InnerVersionLock(it) } ?: InnerVersionLock.NONE


