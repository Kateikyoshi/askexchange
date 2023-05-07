package ru.shirnin.askexchange.business.repo.question


import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.question.DbQuestionRequest

fun MultipleCommandBuilder<InnerQuestionContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Adding Question to DB"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbQuestionRequest(questionRepoPrepare)
        val result = questionRepo.createQuestion(request)
        val resultQuestion = result.data
        if (result.isSuccess && resultQuestion != null) {
            questionRepoDone = resultQuestion
        } else {
            state = InnerState.FAILED
            errors.addAll(result.errors)
        }
    }
}
