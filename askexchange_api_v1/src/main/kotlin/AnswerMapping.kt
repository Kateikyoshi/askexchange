import ru.shirnin.askexchange.api.v1.models.IAnswerRequest
import ru.shirnin.askexchange.api.v1.models.IAnswerResponse

fun apiV1AnswerRequestSerialize(request: IAnswerRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IAnswerRequest> apiV1AnswerRequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IAnswerRequest::class.java) as T

fun apiV1AnswerResponseSerialize(response: IAnswerResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IAnswerResponse> apiV1AnswerResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IAnswerResponse::class.java) as T