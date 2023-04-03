package ru.shirnin.askexchange.app.kafka.strategies.answer

import InnerAnswerContext
import ru.shirnin.askexchange.app.kafka.strategies.ConsumerStrategy

interface AnswerConsumerStrategy: ConsumerStrategy {
    fun serialize(source: InnerAnswerContext): String
    fun deserialize(value: String, target: InnerAnswerContext)
}