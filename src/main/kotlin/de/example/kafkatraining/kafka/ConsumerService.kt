package de.example.kafkatraining.kafka

import mu.KotlinLogging
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class ConsumerService {

    private var listener: MessageListener? = null

    fun register(listener: MessageListener) {
        this.listener = listener
    }

    fun onEvent(message: String) {
        if (listener != null) {
            listener!!.onData(message)
        }
    }

    fun onComplete() {
        if (listener != null) {
            listener!!.processComplete()
        }
    }

    @StreamListener(target = Sink.INPUT)
    fun consume(message: String) {
        logger.info { "received: '$message'" }
        onEvent(message)
    }

}
