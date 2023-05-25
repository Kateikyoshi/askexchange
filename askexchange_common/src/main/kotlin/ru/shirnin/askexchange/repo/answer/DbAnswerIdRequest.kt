package ru.shirnin.askexchange.repo.answer

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer

data class DbAnswerIdRequest (
    val id: InnerId,
    val lock: InnerVersionLock = InnerVersionLock.NONE
) {
    constructor(answer: InnerAnswer): this(answer.id, answer.lock)
}