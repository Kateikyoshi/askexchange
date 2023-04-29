package ru.shirnin.askexchange.repo.tests

import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.QuestionRepository

@OptIn(ExperimentalCoroutinesApi::class)
fun deleteSuccess(repo: QuestionRepository) = funSpec {

    test("deleteSuccess") {
        val deleteSuccess = BaseDelete.initObjects.first()

        runRepoTest {
            val result = repo.deleteQuestion(DbQuestionIdRequest(deleteSuccess))

            result.isSuccess shouldBe true
            result.errors shouldBe emptyList()
        }
    }

}

object BaseDelete : BaseInitQuestions("delete") {
    override val initObjects: List<InnerQuestion> = listOf(
        createInitTestModel("delete"),
        createInitTestModel("deleteLock")
    )
    val idNotFound = InnerId("question-repo-delete-not-found")
}