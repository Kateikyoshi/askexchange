package ru.shirnin.askexchange.business.workers.answer

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker



fun MultipleCommandBuilder<InnerAnswerContext>.initStatus(title: String) = worker {
    this.title = title
    isContextHealthy { state == InnerState.NONE }
    handle { state = InnerState.RUNNING }
}
