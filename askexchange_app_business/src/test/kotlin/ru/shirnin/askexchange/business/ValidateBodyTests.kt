package ru.shirnin.askexchange.business

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.*
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

@OptIn(ExperimentalCoroutinesApi::class)
class ValidateBodyTests: FunSpec({
    val command = InnerCommand.CREATE
    val questionProcessor by lazy { InnerQuestionProcessor() }
    val answerProcessor by lazy { InnerAnswerProcessor() }

    test("validating empty question body") {
        runTest {

            val context = InnerQuestionContext(
                command = command,
                state = InnerState.NONE,
                workMode = InnerWorkMode.TEST,
                questionRequest = InnerQuestion(
                    id = InnerId("111"),
                    title = "abc",
                    body = "",
                    username = "Jack"
                ),
            )
            questionProcessor.exec(context)

            context.errors.forEach {
                println(it)
            }

            context.errors.size shouldBe 1
            context.state shouldBe InnerState.FAILED
        }
    }

    test("validating empty answer body") {
        runTest {

            val context = InnerAnswerContext(
                command = command,
                state = InnerState.NONE,
                workMode = InnerWorkMode.TEST,
                answerRequest = InnerAnswer(
                    id = InnerId("111"),
                    date = Instant.DISTANT_PAST,
                    body = "",
                    likes = 1
                ),
            )
            answerProcessor.exec(context)

            context.errors.size shouldBe 1
            context.state shouldBe InnerState.FAILED
        }
    }
})

