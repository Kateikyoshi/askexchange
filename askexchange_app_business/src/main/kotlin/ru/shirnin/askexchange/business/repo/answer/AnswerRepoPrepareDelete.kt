package ru.shirnin.askexchange.business.repo.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState


fun MultipleCommandBuilder<InnerAnswerContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Preparing for deleting date from DB
    """.trimIndent()
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        answerRepoPrepare = answerValidated.deepCopy()
    }
}
