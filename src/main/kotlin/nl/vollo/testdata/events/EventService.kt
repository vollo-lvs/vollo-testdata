package nl.vollo.testdata.events

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class EventService {

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    private lateinit var kafkaTemplate: KafkaTemplate<String, Any>;

    fun <T: Event<out Any>> send(event: T) {
        kafkaTemplate.send("nl.vollo.testdata." + event.name, event.body)
    }
}