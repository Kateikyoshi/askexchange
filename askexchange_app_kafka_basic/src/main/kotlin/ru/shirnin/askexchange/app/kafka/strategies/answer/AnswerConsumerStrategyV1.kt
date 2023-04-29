package ru.shirnin.askexchange.app.kafka.strategies.answer

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import apiV1AnswerRequestDeserialize
import apiV1AnswerResponseSerialize
import fromTransport
import ru.shirnin.askexchange.api.v1.models.IAnswerRequest
import ru.shirnin.askexchange.api.v1.models.IAnswerResponse
import ru.shirnin.askexchange.app.kafka.config.AppKafkaConfig
import ru.shirnin.askexchange.app.kafka.strategies.InputOutputTopics
import toTransport

class AnswerConsumerStrategyV1 : AnswerConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: InnerAnswerContext): String {
        val response: IAnswerResponse = source.toTransport()
        return apiV1AnswerResponseSerialize(response)
    }

    override fun deserialize(value: String, target: InnerAnswerContext) {
        val request: IAnswerRequest = apiV1AnswerRequestDeserialize(value)
        target.fromTransport(request)
    }
}