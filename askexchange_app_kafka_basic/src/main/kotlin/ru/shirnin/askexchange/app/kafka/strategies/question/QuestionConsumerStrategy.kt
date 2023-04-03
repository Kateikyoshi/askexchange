package ru.shirnin.askexchange.app.kafka.strategies.question

import InnerQuestionContext
import ru.shirnin.askexchange.app.kafka.strategies.ConsumerStrategy

interface QuestionConsumerStrategy: ConsumerStrategy {
    fun serialize(source: InnerQuestionContext): String
    fun deserialize(value: String, target: InnerQuestionContext)
}