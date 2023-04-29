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
    command = ru.shirnin.askexchange.inner.models.InnerCommand.CREATE
    questionRequest = request.toInnerWithUsername()

    debugId = request.obtainDebugId()
    workMode = request.debug?.transportToWorkMode() ?: InnerWorkMode.PROD
    stubCase = request.debug?.transportToStubCase() ?: InnerStubs.NONE
}

private fun InnerQuestionContext.fromTransport(request: QuestionDeleteRequest) {
    command = InnerCommand.DELETE
    questionRequest = request.questionDeleteObject?.questionId?.toInnerOnlyById() ?: InnerQuestion()

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

private fun QuestionCreateRequest.toInnerWithUsername(): InnerQuestion {
    val innerQuestion = this.questionCreateObject?.question?.toInner() ?: InnerQuestion()
    innerQuestion.username = this.questionCreateObject?.username ?: ""
    return innerQuestion
}

private fun String.toInnerOnlyById() = InnerQuestion(id = this.formInnerId())
private fun String.formInnerId() = InnerId(this)


private fun QuestionUpdateRequest.toInnerWithId() = InnerQuestion(
    id = InnerId(this.questionUpdateObject?.questionId ?: ""),
    title = this.questionUpdateObject?.question?.title ?: "",
    body = this.questionUpdateObject?.question?.body ?: "",
    username = ""
)

fun Question.toInner() = InnerQuestion(
    id = InnerId(""),
    title = this.title ?: "",
    body = this.body ?: "",
    username = ""
)


