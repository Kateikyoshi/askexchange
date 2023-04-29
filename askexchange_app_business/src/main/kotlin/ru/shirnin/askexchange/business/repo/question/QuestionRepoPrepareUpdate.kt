package ru.shirnin.askexchange.business.repo.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState

fun MultipleCommandBuilder<InnerQuestionContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Preparing an object for saving in DB"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        questionRepoPrepare = questionFetchedFromRepo.deepCopy().apply {
            this.title = questionValidated.title
            body = questionValidated.body
            username = questionValidated.username
        }
    }
}
