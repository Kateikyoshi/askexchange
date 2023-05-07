package ru.shirnin.askexchange.business.repo.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState

fun MultipleCommandBuilder<InnerAnswerContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Preparing an object for saving in DB"
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        answerRepoPrepare = answerFetchedFromRepo.deepCopy().apply {
            this.body = answerValidated.body
            likes = answerValidated.likes
        }
    }
}
