package nl.vollo.testdata

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VolloTestdataBoot

fun main(args: Array<String>) {
	runApplication<VolloTestdataBoot>(*args)
}

