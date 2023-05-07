import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.inner.models.*
import ru.shirnin.askexchange.inner.models.stubs.InnerStubs

class AnswerMapperTest: FunSpec({
    test("from transport") {
        val body = "Nemesis is in the town"

        val request = AnswerCreateRequest(
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.BAD_ID
            ),
            requestType = "CREATE",
            debugId = "2",
            answerCreateObject = AnswerCreateObject(
                userId = "111",
                questionId = "222",
                answer = Answer(
                    body = body,
                    date = "2023-02-19T18:35:24.00Z",
                    likes = 5
                )
            )
        )

        val context = InnerAnswerContext()
        context.fromTransport(request)

        context.stubCase shouldBe InnerStubs.BAD_ID
        context.workMode shouldBe InnerWorkMode.STUB
        context.answerRequest.body shouldBe body
    }

    test("to transport") {
        val debugId = "111"

        val context = InnerAnswerContext(
            command = InnerCommand.CREATE,
            state = InnerState.FINISHED,
            errors = mutableListOf(InnerError("1"), InnerError("2")),
            workMode = InnerWorkMode.PROD,
            stubCase = InnerStubs.SUCCESS,
            debugId = InnerDebugId(debugId),


            question = InnerQuestion(
                id = InnerId("666"),
                title = "Where am I?",
                body = "I think I'm lost",
                parentUserId = InnerId("888")
            ),
            answerRequest = InnerAnswer(
                id = InnerId("444"),
                body = "body3",
                date = Instant.DISTANT_PAST,
                likes = 500
            ),
            answerResponse = InnerAnswer(
                id = InnerId("777"),
                body = "body4",
                date = Instant.DISTANT_PAST,
                likes = 1100
            )
        )

        val response = context.toTransport() as AnswerCreateResponse

        response.debugId shouldBe debugId
        response.responseType shouldBe "CREATE"
        response.result shouldBe ResponseResult.SUCCESS
        response.answerId shouldBe context.answerResponse.id.asString()
    }
})