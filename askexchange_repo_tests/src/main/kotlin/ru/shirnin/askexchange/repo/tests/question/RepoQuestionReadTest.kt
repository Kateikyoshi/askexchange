package ru.shirnin.askexchange.repo.tests.question

import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.QuestionRepository

@OptIn(ExperimentalCoroutinesApi::class)
fun readSuccess(repo: QuestionRepository) = funSpec {

    test("readSuccess") {
        val readSuccess = BaseRead.initObjects.first()

        runRepoTest {
            val result = repo.readQuestion(DbQuestionIdRequest(readSuccess.id))

            result.isSuccess shouldBe true
            result.data shouldBe readSuccess
            result.errors shouldBe emptyList()
        }
    }
}

object BaseRead : BaseInitQuestions("read") {
    override val initObjects: List<InnerQuestion> = listOf(
        createInitTestModel("read")
    )

    val notFoundId = InnerId("ad-repo-read-not-found")
}

//@OptIn(ExperimentalCoroutinesApi::class)
//abstract class RepoAdReadTest {
//    abstract val repo: IAdRepository
//    protected open val readSucc = initObjects[0]
//
//    @Test
//    fun readSuccess() = runRepoTest {
//        val result = repo.readAd(DbAdIdRequest(readSucc.id))
//
//        assertEquals(true, result.isSuccess)
//        assertEquals(readSucc, result.data)
//        assertEquals(emptyList(), result.errors)
//    }
//
//    @Test
//    fun readNotFound() = runRepoTest {
//        val result = repo.readAd(DbAdIdRequest(notFoundId))
//
//        assertEquals(false, result.isSuccess)
//        assertEquals(null, result.data)
//        val error = result.errors.find { it.code == "not-found" }
//        assertEquals("id", error?.field)
//    }
//
//    companion object : BaseInitAds("delete") {
//        override val initObjects: List<MkplAd> = listOf(
//            createInitTestModel("read")
//        )
//
//        val notFoundId = MkplAdId("ad-repo-read-notFound")
//
//    }
//}