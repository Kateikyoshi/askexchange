package ru.shirnin.askexchange.business.repo.answer


import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.answer.DbAnswerRequest

fun MultipleCommandBuilder<InnerAnswerContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Adding Answer to DB"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbAnswerRequest(answerRepoPrepare)
        val result = answerRepo.createAnswer(request)
        val resultAnswer = result.data
        if (result.isSuccess && resultAnswer != null) {
            answerRepoDone = resultAnswer
        } else {
            state = InnerState.FAILED
            errors.addAll(result.errors)
        }
    }
}
