package ru.shirnin.askexchange.app

import kotlinx.datetime.Clock
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.log.api.v1.models.AnswerContextLogModel
import ru.shirnin.askexchange.log.api.v1.models.AnswerLog
import ru.shirnin.askexchange.log.api.v1.models.CommonAnswerLogModel

fun InnerAnswerContext.toLog(logId: String) = CommonAnswerLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ask-exchange",
    answerContext = answerContextToLog(),
    errors = errors.map { it.toLog() },
)

fun InnerAnswerContext.answerContextToLog(): AnswerContextLogModel? {
    val answerNone = InnerAnswer()

    return AnswerContextLogModel(
        answerRequestLog = answerRequest.takeIf { it != answerNone }?.toLog(),
        answerResponseLog = answerResponse.takeIf { it != answerNone }?.toLog()
    ).takeIf { it != AnswerContextLogModel() }
}

fun InnerAnswer.toLog() = AnswerLog(
    id = id.takeIf { it != InnerId.NONE }?.asString(),
    date = date.toString(),
    body = body.takeIf { it.isNotBlank() },
    likes = likes.toString(),
    parentUserId = parentUserId.takeIf { it != InnerId.NONE }?.asString(),
    parentQuestionId = parentQuestionId.takeIf { it != InnerId.NONE }?.asString()
)