package ru.shirnin.askexchange.repo.tests.question

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.tests.InitObjects

abstract class BaseInitQuestions(private val operation: String): InitObjects<InnerQuestion> {
    open val lockOld: InnerVersionLock = InnerVersionLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: InnerVersionLock = InnerVersionLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suffix: String,
        innerVersionLock: InnerVersionLock = lockOld
    ) = InnerQuestion(
        id = InnerId("question-repo-$operation-$suffix"),
        title = "$suffix stub title",
        body = "$suffix stub body",
        username = "$suffix stub username",
        lock = innerVersionLock
    )
}