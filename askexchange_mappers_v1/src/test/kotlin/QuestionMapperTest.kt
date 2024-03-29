import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.inner.models.*
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs


class QuestionMapperTest: FunSpec({
    test("from transport") {
        val body = "Where is Nemesis?"

        val request = QuestionCreateRequest(
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.BAD_ID
            ),
            requestType = "CREATE",
            debugId = "1",
            questionCreateObject = QuestionCreateObject(
                userId = "1111",
                question = Question(
                    title = "Biohazard",
                    body = body
                )
            )
        )

        val context = InnerQuestionContext()
        context.fromTransport(request)

        context.stubCase shouldBe InnerStubs.BAD_ID
        context.workMode shouldBe InnerWorkMode.STUB
        context.questionRequest.body shouldBe body
    }

    test("to transport") {
        val debugId = "111"

        val context = InnerQuestionContext(
            command = InnerCommand.CREATE,
            state = InnerState.FINISHED,
            errors = mutableListOf(InnerError("1"), InnerError("2")),
            workMode = InnerWorkMode.PROD,
            stubCase = InnerStubs.SUCCESS,
            debugId = InnerDebugId(debugId),
            questionRequest = InnerQuestion(
                id = InnerId("222"),
                title = "title",
                body = "body",
                parentUserId = InnerId("444")
            ),
            questionResponse = InnerQuestion(
                id = InnerId("333"),
                title = "title2",
                body = "body2"
            ),
            answersOfQuestionResponse = mutableListOf(
                InnerAnswer(
                    id = InnerId("444"),
                    body = "body3",
                    date = Instant.DISTANT_PAST,
                    likes = 500
                ),
                InnerAnswer(
                    id = InnerId("555"),
                    body = "body4",
                    date = Instant.DISTANT_PAST,
                    likes = 1100
                )
            )
        )

        val response = context.toTransport() as QuestionCreateResponse

        response.debugId shouldBe debugId
        response.responseType shouldBe "CREATE"
        response.result shouldBe ResponseResult.SUCCESS
        response.questionId shouldBe context.questionResponse.id.asString()
    }
})