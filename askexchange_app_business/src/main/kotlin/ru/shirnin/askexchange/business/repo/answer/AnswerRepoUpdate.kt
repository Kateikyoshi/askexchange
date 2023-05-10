package ru.shirnin.askexchange.business.repo.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.answer.DbAnswerRequest

fun MultipleCommandBuilder<InnerAnswerContext>.repoUpdate(title: String) = worker {
    this.title = title
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbAnswerRequest(answerRepoPrepare)
        val result = answerRepo.updateAnswer(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            answerRepoDone = resultAd
        } else {
            state = InnerState.FAILED
            errors.addAll(result.errors)
            answerRepoDone
        }
    }
}
