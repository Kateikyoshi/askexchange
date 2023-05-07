package ru.shirnin.askexchange.business.repo.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest

fun MultipleCommandBuilder<InnerQuestionContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Deleting question from DB by id"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbQuestionIdRequest(questionRepoPrepare)
        val result = questionRepo.deleteQuestion(request)
        if (!result.isSuccess) {
            state = InnerState.FAILED
            errors.addAll(result.errors)
        }
        questionRepoDone = questionFetchedFromRepo
    }
}
