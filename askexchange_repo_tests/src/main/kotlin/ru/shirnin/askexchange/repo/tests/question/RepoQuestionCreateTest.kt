package ru.shirnin.askexchange.repo.tests.question

import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.QuestionRepository

@OptIn(ExperimentalCoroutinesApi::class)
fun createSuccess(repo: QuestionRepository) = funSpec {

    test("createSuccess") {
        val createObject = InnerQuestion(
            title = "create object",
            body = "create object body",
            username = "create object owner"
        )

        runRepoTest {
            val resultQuestion = repo.createQuestion(DbQuestionRequest(createObject))
            val expectedQuestion = createObject.copy(id = resultQuestion.data?.id ?: InnerId.NONE)

            resultQuestion.isSuccess shouldBe true
            resultQuestion.data?.title shouldBe expectedQuestion.title
            resultQuestion.data?.body shouldBe expectedQuestion.body
            resultQuestion.data?.username shouldBe expectedQuestion.username
        }
    }
}

object BaseCreate : BaseInitQuestions("create") {
    override val initObjects: List<InnerQuestion> = emptyList()
}