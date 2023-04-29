package ru.shirnin.askexchange.inner.models

import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

data class InnerAnswerContext (
    var command: InnerCommand = InnerCommand.NONE,
    var state: InnerState = InnerState.NONE,
    val errors: MutableList<InnerError> = mutableListOf(),

    var workMode: InnerWorkMode = InnerWorkMode.PROD,
    var stubCase: InnerStubs = InnerStubs.NONE,
    var debugId: InnerDebugId = InnerDebugId.NONE,

    var answerValidating: InnerAnswer = InnerAnswer(),
    var answerValidated: InnerAnswer = InnerAnswer(),

    var user: InnerUser = InnerUser(),
    var question: InnerQuestion = InnerQuestion(),

    var answerRequest: InnerAnswer = InnerAnswer(),
    var answerResponse: InnerAnswer = InnerAnswer()
)

