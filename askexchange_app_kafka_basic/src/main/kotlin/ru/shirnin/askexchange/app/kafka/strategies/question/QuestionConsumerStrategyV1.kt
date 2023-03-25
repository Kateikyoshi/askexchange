package ru.shirnin.askexchange.app.kafka.strategies.question

import InnerQuestionContext
import apiV1QuestionRequestDeserialize
import apiV1QuestionResponseSerialize
import fromTransport
import ru.shirnin.askexchange.api.v1.models.IQuestionRequest
import ru.shirnin.askexchange.api.v1.models.IQuestionResponse
import ru.shirnin.askexchange.app.kafka.config.AppKafkaConfig
import ru.shirnin.askexchange.app.kafka.strategies.InputOutputTopics
import toTransport

class QuestionConsumerStrategyV1 : QuestionConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: InnerQuestionContext): String {
        val response: IQuestionResponse = source.toTransport()
        return apiV1QuestionResponseSerialize(response)
    }

    override fun deserialize(value: String, target: InnerQuestionContext) {
        val request: IQuestionRequest = apiV1QuestionRequestDeserialize(value)
        target.fromTransport(request)
    }
}