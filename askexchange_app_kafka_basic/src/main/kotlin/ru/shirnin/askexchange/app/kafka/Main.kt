package ru.shirnin.askexchange.app.kafka

import kotlinx.coroutines.runBlocking
import ru.shirnin.askexchange.app.kafka.config.AppKafkaConfig
import ru.shirnin.askexchange.app.kafka.model.consumer.AnswerKafkaConsumer
import ru.shirnin.askexchange.app.kafka.model.consumer.QuestionKafkaConsumer
import ru.shirnin.askexchange.app.kafka.strategies.answer.AnswerConsumerStrategyV1
import ru.shirnin.askexchange.app.kafka.strategies.question.QuestionConsumerStrategyV1

fun main() {
    val config = AppKafkaConfig()

    runBlocking {
        val questionConsumer = QuestionKafkaConsumer(config, listOf(QuestionConsumerStrategyV1()))
        questionConsumer.run()

        val answerConsumer = AnswerKafkaConsumer(config, listOf(AnswerConsumerStrategyV1()))
        answerConsumer.run()
    }

}