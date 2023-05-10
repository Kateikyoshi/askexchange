package ru.shirnin.askexchange.inner.models

import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs
import ru.shirnin.askexchange.repo.question.QuestionRepository

data class InnerQuestionContext (
    var command: InnerCommand = InnerCommand.NONE,
    var state: InnerState = InnerState.NONE,
    val errors: MutableList<InnerError> = mutableListOf(),

    var workMode: InnerWorkMode = InnerWorkMode.PROD,
    var stubCase: InnerStubs = InnerStubs.NONE,
    var debugId: InnerDebugId = InnerDebugId.NONE,
    var settings: InnerChainSettings = InnerChainSettings.NONE,

    var questionRepo: QuestionRepository = QuestionRepository.NONE,
    var questionFetchedFromRepo: InnerQuestion = InnerQuestion(),
    var questionRepoPrepare: InnerQuestion = InnerQuestion(),
    var questionRepoDone: InnerQuestion = InnerQuestion(),
    var questionsRepoDone: MutableList<InnerQuestion> = mutableListOf(),

    var questionValidating: InnerQuestion = InnerQuestion(),
    var questionValidated: InnerQuestion = InnerQuestion(),

    var questionRequest: InnerQuestion = InnerQuestion(),
    var questionResponse: InnerQuestion = InnerQuestion(),

    var answersOfQuestionResponse: MutableList<InnerAnswer> = mutableListOf()
)