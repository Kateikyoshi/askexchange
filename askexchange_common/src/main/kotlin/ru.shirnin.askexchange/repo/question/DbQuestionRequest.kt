package ru.shirnin.askexchange.repo.question

import ru.shirnin.askexchange.inner.models.question.InnerQuestion

data class DbQuestionRequest (
    val question: InnerQuestion
)