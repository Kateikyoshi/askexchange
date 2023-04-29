package ru.shirnin.askexchange.app.kafka.strategies.answer

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.app.kafka.strategies.ConsumerStrategy

interface AnswerConsumerStrategy: ConsumerStrategy {
    fun serialize(source: InnerAnswerContext): String
    fun deserialize(value: String, target: InnerAnswerContext)
}