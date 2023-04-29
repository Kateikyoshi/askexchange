package ru.shirnin.askexchange.repo.answer

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer

data class DbAnswerIdRequest (
    val id: InnerId
) {
    constructor(answer: InnerAnswer): this(answer.id)
}