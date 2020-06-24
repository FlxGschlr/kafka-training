package de.example.kafkatraining.api

import de.example.kafkatraining.api.resources.MessageResource
import de.example.kafkatraining.kafka.ConsumerService
import de.example.kafkatraining.kafka.MessageListener
import de.example.kafkatraining.kafka.ProducerService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/messages")
class MessageController(
        private val producerService: ProducerService,
        private val consumerService: ConsumerService) {

    private val bridge: Flux<String>

    init {
        bridge = createBridge().publish().autoConnect().cache(10).log();
    }

    private fun createBridge(): Flux<String>  {
        return Flux.create {
            sink -> run {
            consumerService.register( object : MessageListener {
                override fun onData(message: String) {
                    sink.next(message)
                }
                override fun processComplete() {
                    sink.complete()
                }
            })
        }
        }
    }

    @PostMapping
    fun sendMessage(@RequestBody resource: MessageResource) {
        producerService.send(resource.message, "messages")
    }

    @GetMapping(produces = ["text/event-stream;charset=UTF-8"])
    fun getMessages(): Flux<String> = bridge

}
