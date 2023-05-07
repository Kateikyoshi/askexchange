package ru.shirnin.askexchange.business.repo.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.answer.DbAnswerIdRequest

fun MultipleCommandBuilder<InnerAnswerContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Deleting answer from DB by id"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbAnswerIdRequest(answerRepoPrepare)
        val result = answerRepo.deleteAnswer(request)
        if (!result.isSuccess) {
            state = InnerState.FAILED
            errors.addAll(result.errors)
        }
        answerRepoDone = answerFetchedFromRepo
    }
}
