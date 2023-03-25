package ru.shirnin.askexchange

import apiV1QuestionRequestSerialize
import apiV1QuestionResponseDeserialize
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.app.kafka.config.AppKafkaConfig
import ru.shirnin.askexchange.app.kafka.model.consumer.QuestionKafkaConsumer
import ru.shirnin.askexchange.app.kafka.strategies.question.QuestionConsumerStrategyV1
import java.util.*

class KafkaControllerTest : FunSpec({
    test("run kafka") {
        val partition = 0

        val consumerMock = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producerMock = MockProducer(true, StringSerializer(), StringSerializer())

        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val questionKafkaConsumer = QuestionKafkaConsumer(
            config, listOf(QuestionConsumerStrategyV1()),
            consumer = consumerMock, producer = producerMock
        )

        val questionCreateRequest = QuestionCreateRequest(
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

        consumerMock.schedulePollTask {
            consumerMock.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumerMock.addRecord(
                ConsumerRecord(
                    inputTopic,
                    partition,
                    0L,
                    "test",
                    apiV1QuestionRequestSerialize(questionCreateRequest)
                )
            )
            questionKafkaConsumer.stop()
        }

        val topicPartition = TopicPartition(inputTopic, partition)
        val startOffsets: Map<TopicPartition, Long> = mapOf(topicPartition to 0L)
        consumerMock.updateBeginningOffsets(startOffsets)

        questionKafkaConsumer.run()

        val message = producerMock.history().first()
        val result = apiV1QuestionResponseDeserialize<QuestionCreateResponse>(message.value())

        outputTopic shouldBe message.topic()
        result.questionId shouldBe "1"
    }
})


