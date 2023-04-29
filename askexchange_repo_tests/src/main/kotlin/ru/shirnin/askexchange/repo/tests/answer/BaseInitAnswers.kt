package ru.shirnin.askexchange.repo.tests.answer

import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.repo.tests.InitObjects

abstract class BaseInitAnswers(private val operation: String): InitObjects<InnerAnswer> {

    fun createInitTestModel(
        suffix: String,
    ) = InnerAnswer(
        id = InnerId("question-repo-$operation-$suffix"),
        body = "$suffix stub body",
        date = Instant.DISTANT_PAST,
        likes = 999
    )
}