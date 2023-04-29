package ru.shirnin.askexchange.app.v1

import io.ktor.server.application.*
import ru.shirnin.askexchange.app.AskAppSettings
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.logging.common.AskLogWrapper

private val clazzCreate = ApplicationCall::createQuestion::class.qualifiedName ?: "create"

suspend fun ApplicationCall.createQuestion(appSettings: AskAppSettings, log: AskLogWrapper) =
    processQuestionV1<QuestionCreateRequest, QuestionCreateResponse>(appSettings, log, "question-create", InnerCommand.CREATE)

suspend fun ApplicationCall.deleteQuestion(appSettings: AskAppSettings, log: AskLogWrapper) =
    processQuestionV1<QuestionDeleteRequest, QuestionDeleteResponse>(appSettings, log, "question-delete", InnerCommand.DELETE)

suspend fun ApplicationCall.updateQuestion(appSettings: AskAppSettings, log: AskLogWrapper) =
    processQuestionV1<QuestionUpdateRequest, QuestionUpdateResponse>(appSettings, log, "question-update", InnerCommand.UPDATE)

suspend fun ApplicationCall.readQuestion(appSettings: AskAppSettings, log: AskLogWrapper) =
    processQuestionV1<QuestionReadRequest, QuestionReadResponse>(appSettings, log, "question-read", InnerCommand.READ)