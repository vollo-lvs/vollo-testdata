package nl.vollo.testdata.events.listeners

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.util.*
import javax.jms.Message

@Component
class OnLeerlingOpgehaald {

    @JmsListener(destination = "nl.vollo.kern.LeerlingOpgehaald")
    fun receive(message: Message) {

        println("message from ${Date(message.jmsTimestamp)}: ${message}")
    }
}