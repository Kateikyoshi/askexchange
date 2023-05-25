package ru.shirnin.askexchange.repo.answer

import ru.shirnin.askexchange.inner.models.answer.InnerAnswer

data class DbAnswerRequest (
    val answer: InnerAnswer
)