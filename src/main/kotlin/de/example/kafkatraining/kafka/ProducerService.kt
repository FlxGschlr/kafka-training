package de.example.kafkatraining.kafka

import mu.KotlinLogging
import org.springframework.cloud.stream.messaging.Source
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class ProducerService(private val source: Source) {

    fun send(message: String, topic: String) {
        source.output().send(MessageBuilder.withPayload(message).build())
        logger.info { "send: '$message'" }
    }
}
