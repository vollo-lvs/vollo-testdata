package nl.vollo.testdata.events.listeners

import com.fasterxml.jackson.databind.JsonNode
import nl.vollo.testdata.events.EventService
import nl.vollo.testdata.events.LeerlingFotoVerkregen
import nl.vollo.testdata.model.Leerling
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.io.File
import java.net.URI
import java.net.URL

@Component
class OnLeerlingOpgehaald {

    @Autowired
    private lateinit var eventService: EventService

    @KafkaListener(topics = ["nl.vollo.kern.LeerlingOpgehaald"], groupId = "volloKern")
    fun receive(json: JsonNode) {
        val id = json.get("id").asLong()
        val geslacht = json.get("geslacht").asText()
        println("message: ${id} ${geslacht}")
        val leerling = Leerling(id, geslacht)
        leerling.foto = IOUtils.toByteArray(URL("http://avataaars.io/?avatarStyle=Circle"))

        eventService.send(LeerlingFotoVerkregen(leerling))
//        kafkaTemplate.send("nl.vollo.testdata.LeerlingFotoVerkregen", "http://avataaars.io/?avatarStyle=Circle")
    }

}
