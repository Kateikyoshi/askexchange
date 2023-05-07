package ru.shirnin.askexchange.business.repo.question

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState


fun MultipleCommandBuilder<InnerQuestionContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Preparing for deleting date from DB
    """.trimIndent()
    isContextHealthy { state == InnerState.RUNNING }
    handle {
        questionRepoPrepare = questionValidated.deepCopy()
    }
}
