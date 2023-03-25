import ru.shirnin.askexchange.api.v1.models.IQuestionRequest
import ru.shirnin.askexchange.api.v1.models.IQuestionResponse

fun apiV1QuestionRequestSerialize(request: IQuestionRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IQuestionRequest> apiV1QuestionRequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IQuestionRequest::class.java) as T

fun apiV1QuestionResponseSerialize(response: IQuestionResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IQuestionResponse> apiV1QuestionResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IQuestionResponse::class.java) as T