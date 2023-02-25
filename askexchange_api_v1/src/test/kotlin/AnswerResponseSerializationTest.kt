import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ru.shirnin.askexchange.api.v1.models.AnswerCreateResponse
import ru.shirnin.askexchange.api.v1.models.Error
import ru.shirnin.askexchange.api.v1.models.IAnswerResponse
import ru.shirnin.askexchange.api.v1.models.ResponseResult

class AnswerResponseSerializationTest : FunSpec({
    val answerId = "222"
    val responseType = "CREATE"

    val response = AnswerCreateResponse(
        responseType = responseType,
        debugId = "111",
        result = ResponseResult.ERROR,
        errors = listOf(Error("1"), Error("2")),
        answerId = answerId
    )

    test("serialize") {
        val json = apiV1Mapper.writeValueAsString(response)

        json shouldContain """"responseType":\s?"$responseType"""".trimIndent().toRegex()
        json shouldContain """"answerId":\s?"$answerId"""".trimIndent().toRegex()
    }

    test("deserialize") {
        val json = apiV1Mapper.writeValueAsString(response)
        val backwards = apiV1Mapper.readValue(json, IAnswerResponse::class.java) as AnswerCreateResponse

        backwards shouldBe response
    }
})
