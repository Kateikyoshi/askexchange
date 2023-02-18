import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ru.shirnin.askexchange.api.v1.models.*

class QuestionResponseSerializationTest: FunSpec({
    val questionId = "222"
    val responseType = "CREATE"

    val response = QuestionCreateResponse(
        responseType = responseType,
        debugId = "111",
        result = ResponseResult.ERROR,
        errors = listOf(Error("1"), Error("2")),
        questionId = questionId
    )

    test("serialize") {
        val json = apiV1Mapper.writeValueAsString(response)

        json shouldContain """"responseType":\s?"$responseType"""".trimIndent().toRegex()
        json shouldContain """"questionId":\s?"$questionId"""".trimIndent().toRegex()
    }

    test("deserialize") {
        val json = apiV1Mapper.writeValueAsString(response)
        val backwards = apiV1Mapper.readValue(json, IQuestionResponse::class.java) as QuestionCreateResponse

        backwards shouldBe response
    }
})