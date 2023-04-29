package ru.shirnin.askexchange.business.repo.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.question.DbQuestionRequest

fun MultipleCommandBuilder<InnerQuestionContext>.repoUpdate(title: String) = worker {
    this.title = title
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbQuestionRequest(questionRepoPrepare)
        val result = questionRepo.updateQuestion(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            questionRepoDone = resultAd
        } else {
            state = InnerState.FAILED
            errors.addAll(result.errors)
            questionRepoDone
        }
    }
}
