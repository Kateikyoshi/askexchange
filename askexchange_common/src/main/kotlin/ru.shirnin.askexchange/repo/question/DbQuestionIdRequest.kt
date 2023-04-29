package ru.shirnin.askexchange.repo.question

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

data class DbQuestionIdRequest (
    val id: InnerId
) {
    constructor(question: InnerQuestion): this(question.id)
}