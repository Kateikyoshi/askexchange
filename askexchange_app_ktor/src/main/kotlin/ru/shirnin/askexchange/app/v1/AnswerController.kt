package ru.shirnin.askexchange.app.v1

import io.ktor.server.application.*
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.app.AskAppSettings
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.logging.common.AskLogWrapper

suspend fun ApplicationCall.createAnswer(appSettings: AskAppSettings, log: AskLogWrapper) =
    processAnswerV1<AnswerCreateRequest, AnswerCreateResponse>(appSettings, log, "answer-create", InnerCommand.CREATE)

suspend fun ApplicationCall.deleteAnswer(appSettings: AskAppSettings, log: AskLogWrapper) =
    processAnswerV1<AnswerDeleteRequest, AnswerDeleteResponse>(appSettings, log, "answer-delete", InnerCommand.DELETE)

suspend fun ApplicationCall.updateAnswer(appSettings: AskAppSettings, log: AskLogWrapper) =
    processAnswerV1<AnswerUpdateRequest, AnswerUpdateResponse>(appSettings, log, "answer-update", InnerCommand.UPDATE)

suspend fun ApplicationCall.readAnswer(appSettings: AskAppSettings, log: AskLogWrapper) =
    processAnswerV1<AnswerReadRequest, AnswerReadResponse>(appSettings, log, "answer-read", InnerCommand.READ)