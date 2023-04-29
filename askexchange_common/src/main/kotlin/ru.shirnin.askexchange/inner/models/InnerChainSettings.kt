package ru.shirnin.askexchange.inner.models

import ru.shirnin.askexchange.logging.common.LoggerProvider
import ru.shirnin.askexchange.repo.question.QuestionRepository

data class InnerChainSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoStub: QuestionRepository = QuestionRepository.NONE,
    val repoTest: QuestionRepository = QuestionRepository.NONE,
    val repoProd: QuestionRepository = QuestionRepository.NONE
) {
    companion object {
        @Suppress("unused")
        val NONE = InnerChainSettings()
    }
}