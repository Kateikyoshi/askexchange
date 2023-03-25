package ru.shirnin.askexchange.app.kafka.strategies

import ru.shirnin.askexchange.app.kafka.config.AppKafkaConfig

interface ConsumerStrategy {
    fun topics(config: AppKafkaConfig): InputOutputTopics
}