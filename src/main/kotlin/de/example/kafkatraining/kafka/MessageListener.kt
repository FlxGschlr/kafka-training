package de.example.kafkatraining.kafka

interface MessageListener {
    fun onData(message: String)
    fun processComplete()
}
