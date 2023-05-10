package ru.shirnin.askexchange.repo.tests.answer

import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.repo.tests.InitObjects

abstract class BaseInitAnswers(private val operation: String): InitObjects<InnerAnswer> {
    open val lockOld: InnerVersionLock = InnerVersionLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: InnerVersionLock = InnerVersionLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suffix: String,
        innerVersionLock: InnerVersionLock = lockOld
    ) = InnerAnswer(
        id = InnerId("question-repo-$operation-$suffix"),
        body = "$suffix stub body",
        date = Instant.DISTANT_PAST,
        likes = 999,
        lock = innerVersionLock,
        parentUserId = InnerId("$suffix stub parentUserId"),
        parentQuestionId = InnerId("$suffix stub parentQuestionId")
    )
}