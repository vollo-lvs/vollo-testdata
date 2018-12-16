package nl.vollo.testdata.events.listeners

import nl.vollo.events.EventService
import nl.vollo.events.kern.LeerlingOpgehaald
import nl.vollo.events.testdata.LeerlingFotoVerkregen
import nl.vollo.testdata.model.Leerling
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.getForEntity
import java.net.URL

@Component
class OnLeerlingOpgehaald {

    @Autowired
    private lateinit var eventService: EventService

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @KafkaListener(topics = [LeerlingOpgehaald.TOPIC])
    fun receive(event: LeerlingOpgehaald) {
        val id = event.id
        val geslacht = event.geslacht
        println("message: ${id} ${geslacht}")

        val headers = LinkedMultiValueMap<String, String>()
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Safari/605.1.15")

        val response: ResponseEntity<ByteArray> = restTemplate.exchange(
                "http://avataaars.io/?avatarStyle=Circle",
                HttpMethod.GET,
                HttpEntity<ByteArray>(headers),
                ByteArray::class)

        val foto = response.body

//        val foto = IOUtils.toByteArray(URL("http://avataaars.io/?avatarStyle=Circle"))

        eventService.send(LeerlingFotoVerkregen().apply { this.id = id; this.foto = foto })
    }

}
