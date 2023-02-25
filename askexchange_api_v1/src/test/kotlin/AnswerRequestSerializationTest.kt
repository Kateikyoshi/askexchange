import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ru.shirnin.askexchange.api.v1.models.*

class AnswerRequestSerializationTest: FunSpec({
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
                body = "Nemesis is in the town",
                date = "2023-02-19",
                likes = 5
            )
        )
    )

    test("serialize") {
        val json = apiV1Mapper.writeValueAsString(request)

        json shouldContain """"body":\s?"Nemesis is in the town"""".trimIndent().toRegex()
        json shouldContain """"userId":\s?"111"""".trimIndent().toRegex()
    }

    test("deserialize") {
        val json = apiV1Mapper.writeValueAsString(request)
        val backwards = apiV1Mapper.readValue(json, IAnswerRequest::class.java) as AnswerCreateRequest

        backwards shouldBe request
    }
})