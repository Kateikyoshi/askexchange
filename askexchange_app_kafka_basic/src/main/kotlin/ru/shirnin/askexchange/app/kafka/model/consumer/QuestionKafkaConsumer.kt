package ru.shirnin.askexchange.app.kafka.model.consumer

import InnerQuestionContext
import InnerQuestionStub
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.*
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import ru.shirnin.askexchange.app.kafka.config.AppKafkaConfig
import ru.shirnin.askexchange.app.kafka.model.createKafkaConsumer
import ru.shirnin.askexchange.app.kafka.model.createKafkaProducer
import ru.shirnin.askexchange.app.kafka.strategies.question.QuestionConsumerStrategy
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

class InnerQuestionProcessor {
    fun exec(ctx: InnerQuestionContext) {
        ctx.questionResponse = InnerQuestionStub.get()
    }
}

class QuestionKafkaConsumer(
    private val config: AppKafkaConfig,
    questionConsumerStrategies: List<QuestionConsumerStrategy>,
    private val processor: InnerQuestionProcessor = InnerQuestionProcessor(),
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer()
) {
    private val process = atomic(true)
    private val topicsAndStrategyByInputTopic = questionConsumerStrategies.associate {
        val topics = it.topics(config)
        Pair(
            topics.input,
            TopicsAndStrategy(topics.input, topics.output, it)
        )
    }

    suspend fun run() {
        try {
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)
            while (process.value) {
                val ctx = InnerQuestionContext()

                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                if (!records.isEmpty)
                    log.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        log.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }
                        val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()]
                            ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        strategy.deserialize(record.value(), ctx)
                        processor.exec(ctx)

                        sendResponse(ctx, strategy, outputTopic)
                    } catch (ex: Exception) {
                        log.error(ex) { "error" }
                    }
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(context: InnerQuestionContext, strategy: QuestionConsumerStrategy, outputTopic: String) {
        val json = strategy.serialize(context)
        val resRecord = ProducerRecord(
            outputTopic,
            UUID.randomUUID().toString(),
            json
        )
        log.info { "sending ${resRecord.key()} to $outputTopic:\n$json" }
        producer.send(resRecord)
    }

    fun stop() {
        process.value = false
    }

    private data class TopicsAndStrategy(val inputTopic: String, val outputTopic: String, val questionStrategy: QuestionConsumerStrategy)
}