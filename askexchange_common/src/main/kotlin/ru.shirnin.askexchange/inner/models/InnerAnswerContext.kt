package ru.shirnin.askexchange.inner.models

import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs
import ru.shirnin.askexchange.repo.answer.AnswerRepository

data class InnerAnswerContext (
    var command: InnerCommand = InnerCommand.NONE,
    var state: InnerState = InnerState.NONE,
    val errors: MutableList<InnerError> = mutableListOf(),

    var workMode: InnerWorkMode = InnerWorkMode.PROD,
    var stubCase: InnerStubs = InnerStubs.NONE,
    var debugId: InnerDebugId = InnerDebugId.NONE,
    var settings: InnerChainSettings = InnerChainSettings.NONE,

    var answerRepo: AnswerRepository = AnswerRepository.NONE,
    var answerFetchedFromRepo: InnerAnswer = InnerAnswer(),
    var answerRepoPrepare: InnerAnswer = InnerAnswer(),
    var answerRepoDone: InnerAnswer = InnerAnswer(),
    var answersRepoDone: MutableList<InnerAnswer> = mutableListOf(),

    var answerValidating: InnerAnswer = InnerAnswer(),
    var answerValidated: InnerAnswer = InnerAnswer(),

    /**
     * Question parent of that answer
     */
    var question: InnerQuestion = InnerQuestion(),

    var answerRequest: InnerAnswer = InnerAnswer(),
    var answerResponse: InnerAnswer = InnerAnswer()
)

