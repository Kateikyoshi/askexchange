package ru.shirnin.askexchange.inner.models

import ru.shirnin.askexchange.logging.common.LoggerProvider
import ru.shirnin.askexchange.repo.answer.AnswerRepository
import ru.shirnin.askexchange.repo.question.QuestionRepository

data class InnerChainSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val questionRepoStub: QuestionRepository = QuestionRepository.NONE,
    val questionRepoTest: QuestionRepository = QuestionRepository.NONE,
    val questionRepoProd: QuestionRepository = QuestionRepository.NONE,
    val answerRepoStub: AnswerRepository = AnswerRepository.NONE,
    val answerRepoTest: AnswerRepository = AnswerRepository.NONE,
    val answerRepoProd: AnswerRepository = AnswerRepository.NONE,
) {
    companion object {
        @Suppress("unused")
        val NONE = InnerChainSettings()
    }
}