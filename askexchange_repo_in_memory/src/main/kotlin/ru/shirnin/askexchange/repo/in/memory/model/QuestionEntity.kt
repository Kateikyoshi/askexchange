package ru.shirnin.askexchange.repo.`in`.memory.model

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

data class QuestionEntity (
    val id: String? = null,
    val title: String? = null,
    val body: String? = null,
    val parentUserId: String? = null,
    val lock: String? = null
) {
    constructor(innerQuestion: InnerQuestion): this(
        id = innerQuestion.id.asString().takeIf { it.isNotBlank() },
        title = innerQuestion.title.takeIf { it.isNotBlank() },
        body = innerQuestion.body.takeIf { it.isNotBlank() },
        parentUserId = innerQuestion.parentUserId.asString().takeIf { it.isNotBlank() },
        lock = innerQuestion.lock.asString()
    )

    fun toInner() = InnerQuestion(
        id = id?.let { InnerId(it) } ?: InnerId.NONE,
        title = title ?: "",
        body = body ?: "",
        parentUserId = parentUserId?.let { InnerId(it) } ?: InnerId.NONE,
        lock = lock?.let { InnerVersionLock(it) } ?: InnerVersionLock.NONE
    )
}