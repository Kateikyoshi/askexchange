package ru.shirnin.askexchange.repo.tests

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

abstract class BaseInitQuestions(private val operation: String): InitObjects<InnerQuestion> {

    fun createInitTestModel(
        suffix: String,
    ) = InnerQuestion(
        id = InnerId("question-repo-$operation-$suffix"),
        title = "$suffix stub title",
        body = "$suffix stub body",
        username = "$suffix stub username"
    )
}