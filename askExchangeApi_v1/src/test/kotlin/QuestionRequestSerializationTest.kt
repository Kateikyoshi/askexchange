import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ru.shirnin.askexchange.api.v1.models.*

class QuestionRequestSerializationTest: FunSpec({
    val request = QuestionCreateRequest(
        debug = Debug(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.BAD_ID
        ),
        requestType = "CREATE",
        debugId = "1",
        questionCreateObject = QuestionCreateObject(
            username = "Jill",
            question = Question(
                title = "Biohazard",
                body = "Where is Nemesis?"
            )
        )
    )

    test("serialize") {
        val json = apiV1Mapper.writeValueAsString(request)

        json shouldContain """"title":\s?"Biohazard"""".trimIndent().toRegex()
        json shouldContain """"username":\s?"Jill"""".trimIndent().toRegex()
    }

    test("deserialize") {
        val json = apiV1Mapper.writeValueAsString(request)
        val backwards = apiV1Mapper.readValue(json, IQuestionRequest::class.java) as QuestionCreateRequest

        backwards shouldBe request
    }
})