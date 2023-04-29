package ru.shirnin.askexchange.business.repo.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest

fun MultipleCommandBuilder<InnerQuestionContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        val request = DbQuestionIdRequest(questionValidated)
        val result = questionRepo.readQuestion(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            questionFetchedFromRepo = resultAd
        } else {
            state = InnerState.FAILED
            errors.addAll(result.errors)
        }
    }
}
