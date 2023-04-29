package ru.shirnin.askexchange.repo.`in`.memory.model

import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer

data class AnswerEntity (
    val id: String? = null,
    val date: String? = null,
    val body: String? = null,
    val likes: String? = null
) {
    constructor(innerAnswer: InnerAnswer): this(
        id = innerAnswer.id.asString().takeIf { it.isNotBlank() },
        date = innerAnswer.date.toString(),
        body = innerAnswer.body.takeIf { it.isNotBlank() },
        likes = innerAnswer.likes.toString()
    )

    fun toInner() = InnerAnswer(
        id = id?.let { InnerId(it) } ?: InnerId.NONE,
        date = date?.let { Instant.parse(it) } ?: Instant.DISTANT_PAST,
        body = body ?: "",
        likes = likes?.toLong() ?: 0
    )
}