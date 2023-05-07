import exceptions.UnknownRequestClass
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.inner.models.*
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

fun InnerQuestionContext.fromTransport(request: IQuestionRequest) = when (request) {
    is QuestionCreateRequest -> fromTransport(request)
    is QuestionDeleteRequest -> fromTransport(request)
    is QuestionUpdateRequest -> fromTransport(request)
    is QuestionReadRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun IQuestionRequest.obtainDebugId(): InnerDebugId {
    val debugId: String = if (this.debugId != null) this.debugId!! else ""
    return InnerDebugId(debugId)
}


private fun InnerQuestionContext.fromTransport(request: QuestionCreateRequest) {
    command = InnerCommand.CREATE
    questionRequest = request.toInnerWithParentUserAndQuestionId()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerQuestionContext.fromTransport(request: QuestionDeleteRequest) {
    command = InnerCommand.DELETE
    questionRequest = request.toInnerWithId()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerQuestionContext.fromTransport(request: QuestionUpdateRequest) {
    command = InnerCommand.UPDATE
    questionRequest = request.toInnerWithId()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerQuestionContext.fromTransport(request: QuestionReadRequest) {
    command = InnerCommand.READ
    questionRequest = request.questionReadObject?.questionId?.toInnerOnlyById() ?: InnerQuestion()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun QuestionCreateRequest.toInnerWithParentUserAndQuestionId(): InnerQuestion {
    val innerQuestion = this.questionCreateObject?.question?.toInner() ?: InnerQuestion()
    innerQuestion.parentUserId = this.questionCreateObject?.userId.formInnerId()
    return innerQuestion
}

private fun String?.toInnerOnlyById() = InnerQuestion(id = this.formInnerId())
private fun String?.formInnerId() = this?.let { InnerId(it) } ?: InnerId.NONE

private fun String?.toInnerVersionLock() = this?.let { InnerVersionLock(it) }  ?: InnerVersionLock.NONE


private fun QuestionUpdateRequest.toInnerWithId() = InnerQuestion(
    id = InnerId(this.questionUpdateObject?.questionId ?: ""),
    title = this.questionUpdateObject?.question?.title ?: "",
    body = this.questionUpdateObject?.question?.body ?: "",
    lock = this.questionUpdateObject?.versionLock.toInnerVersionLock()
)

private fun QuestionDeleteRequest.toInnerWithId() = InnerQuestion(
    id = InnerId(this.questionDeleteObject?.questionId ?: ""),
    lock = this.questionDeleteObject?.versionLock.toInnerVersionLock()
)

fun Question.toInner() = InnerQuestion(
    id = InnerId(""),
    title = this.title ?: "",
    body = this.body ?: ""
)


