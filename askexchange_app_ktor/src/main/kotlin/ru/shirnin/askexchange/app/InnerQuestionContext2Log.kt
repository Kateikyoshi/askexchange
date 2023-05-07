package ru.shirnin.askexchange.app

import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import kotlinx.datetime.Clock
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.log.api.v1.models.CommonQuestionLogModel
import ru.shirnin.askexchange.log.api.v1.models.ErrorLogModel
import ru.shirnin.askexchange.log.api.v1.models.QuestionContextLogModel
import ru.shirnin.askexchange.log.api.v1.models.QuestionLog

fun InnerQuestionContext.toLog(logId: String) = CommonQuestionLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ask-exchange",
    questionContext = questionContextToLog(),
    errors = errors.map { it.toLog() },
)

fun InnerQuestionContext.questionContextToLog(): QuestionContextLogModel? {
    val questionNone = InnerQuestion()

    return QuestionContextLogModel(
        questionRequestLog = questionRequest.takeIf { it != questionNone }?.toLog(),
        questionResponseLog = questionResponse.takeIf { it != questionNone }?.toLog()
    ).takeIf { it != QuestionContextLogModel() }
}

fun InnerError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() }
)

fun InnerQuestion.toLog() = QuestionLog(
    id = id.takeIf { it != InnerId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    body = body.takeIf { it.isNotBlank() },
    parentUserId = parentUserId.takeIf { it != InnerId.NONE }?.asString()
)