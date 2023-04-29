package ru.shirnin.askexchange.repo.tests.question

import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.QuestionRepository

@OptIn(ExperimentalCoroutinesApi::class)
fun updateSuccess(repo: QuestionRepository) = funSpec {

    test("updateSuccess") {
        val updateSuccess = BaseUpdate.initObjects.first()

        val reqUpdateSucc by lazy {
            InnerQuestion(
                id = updateSuccess.id,
                title = updateSuccess.title,
                body = updateSuccess.body,
                username = updateSuccess.username
            )
        }


        runRepoTest {
            val result = repo.updateQuestion(DbQuestionRequest(reqUpdateSucc))

            result.isSuccess shouldBe true
            result.data?.id shouldBe reqUpdateSucc.id
            result.data?.body shouldBe reqUpdateSucc.body
            result.data?.title shouldBe reqUpdateSucc.title
            result.data?.username shouldBe reqUpdateSucc.username
            result.errors shouldBe emptyList()
        }
    }
}

object BaseUpdate : BaseInitQuestions("update") {
    override val initObjects: List<InnerQuestion> = listOf(
        createInitTestModel("update"),
        createInitTestModel("updateConc")
    )
}