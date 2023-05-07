package ru.shirnin.askexchange.repo.question

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

data class DbQuestionIdRequest (
    val id: InnerId,
    val lock: InnerVersionLock = InnerVersionLock.NONE
) {
    constructor(question: InnerQuestion): this(question.id, question.lock)
}