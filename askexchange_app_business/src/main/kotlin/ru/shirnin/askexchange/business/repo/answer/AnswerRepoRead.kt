package ru.shirnin.askexchange.business.repo.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.answer.DbAnswerIdRequest

fun MultipleCommandBuilder<InnerAnswerContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Reading Answer from DB"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbAnswerIdRequest(answerValidated)
        val result = answerRepo.readAnswer(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            answerFetchedFromRepo = resultAd
        } else {
            state = InnerState.FAILED
            errors.addAll(result.errors)
        }
    }
}
