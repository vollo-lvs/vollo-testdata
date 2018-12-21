package nl.vollo.testdata.events.listeners

import nl.vollo.events.EventService
import nl.vollo.events.kern.LeerlingOpgehaald
import nl.vollo.events.testdata.LeerlingFotoVerkregen
import nl.vollo.testdata.model.Leerling
import nl.vollo.testdata.repository.LeerlingRepository
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.random.Random

@Component
class OnLeerlingOpgehaald {

    @Autowired
    private lateinit var eventService: EventService

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var leerlingRepository: LeerlingRepository;

    var random: Random? = null;

    final fun mv(listm: List<String>, listv: List<String> = listm): Map<String, List<String>> {
        return mapOf(
                "MAN" to listm,
                "VROUW" to listv,
                "OVERIG" to listm.union(listv).toList()
        )
    }

    final var topTypes: Map<String, List<String>> = mv(
            listOf("NoHair",
                    "Hat",
                    "Turban",
                    "LongHairBun",
                    "ShortHairDreads01",
                    "ShortHairDreads02",
                    "ShortHairFrizzle",
                    "ShortHairShaggyMullet",
                    "ShortHairShortCurly",
                    "ShortHairShortFlat",
                    "ShortHairShortRound",
                    "ShortHairShortWaved",
                    "ShortHairSides",
                    "ShortHairTheCaesar",
                    "ShortHairTheCaesarSidePart"),
            listOf(
                    "Hijab",
                    "LongHairBigHair",
                    "LongHairBob",
                    "LongHairCurly",
                    "LongHairCurvy",
                    "LongHairDreads",
                    "LongHairFrida",
                    "LongHairFro",
                    "LongHairFroBand",
                    "LongHairNotTooLong",
                    "LongHairShavedSides",
                    "LongHairMiaWallace",
                    "LongHairStraight",
                    "LongHairStraight2",
                    "LongHairStraightStrand"
            )
    )
    
    final var accessoriesTypes = mv(listOf(
            "Blank", "Prescription01", "Prescription02", "Round"
    ))
    
    final var hairColors = mv(listOf(
            "Auburn",
            "Black",
            "Blonde",
            "BlondeGolden",
            "Brown",
            "BrownDark",
            "PastelPink",
            "Platinum",
            "Red",
            "SilverGray"
    ))

    final var clotheTypes = mv(listOf(
            "BlazerShirt",
            "BlazerSweater",
            "CollarSweater",
            "GraphicShirt",
            "Hoodie",
            "Overall",
            "ShirtCrewNeck",
            "ShirtScoopNeck",
            "ShirtVNeck"
    ))

    final var clotheColors = mv(listOf(
            "Black",
            "Blue01",
            "Blue02",
            "Blue03",
            "Gray01",
            "Gray02",
            "Heather",
            "PastelBlue",
            "PastelGreen",
            "PastelOrange",
            "PastelRed",
            "PastelYellow",
            "Pink",
            "Red",
            "White"
    ))

    final var eyeTypes = mv(listOf(
                "Close",
                "Cry",
                "Default",
                "Dizzy",
                "EyeRoll",
                "Happy",
                "Side",
                "Squint",
                "Surprised",
                "Wink",
                "WinkWacky"
    ), listOf(
                "Close",
                "Cry",
                "Default",
                "Dizzy",
                "EyeRoll",
                "Happy",
                "Hearts",
                "Side",
                "Squint",
                "Surprised",
                "Wink",
                "WinkWacky"
    ))

    final var eyebrowTypes = mv(listOf(
            "Angry",
            "AngryNatural",
            "Default",
            "DefaultNatural",
            "FlatNatural",
            "RaisedExcited",
            "RaisedExcitedNatural",
            "SadConcerned",
            "SadConcernedNatural",
            "UnibrowNatural",
            "UpDown",
            "UpDownNatural"
    ))

    final var mouthTypes = mv(listOf(
            "Concerned",
            "Default",
            "Disbelief",
            "Eating",
            "Grimace",
            "Sad",
            "Serious",
            "Smile",
            "Tongue"
    ))

    final var skinColors = mv(listOf("Tanned", "Yellow", "Pale", "Light", "Brown", "DarkBrown", "Black"))

    final var attrLists: Map<String, Map<String, List<String>>> = mapOf(
            "topType" to topTypes,
            "acccessoriesType" to accessoriesTypes,
            "hairColor" to hairColors,
            "clotheType" to clotheTypes,
            "clotheColor" to clotheColors,
            "eyeType" to eyeTypes,
            "eyebrowType" to eyebrowTypes,
            "mouthType" to mouthTypes,
            "skinColor" to skinColors
    )

    fun randomElem(list: List<String>): String =
        list[Math.floor(random!!.nextDouble() * list.size).toInt()]

    fun attr(attrType: String, geslacht: String): String =
            randomElem(attrLists.getValue(attrType).getValue(geslacht))

    fun params(geslacht: String) = attrLists.keys
            .fold(LinkedMultiValueMap<String, String>()) { map, next ->
                map.set(next, attr(next, geslacht))
                map
            }

    fun svg2png(svgInputStream: InputStream): ByteArray? {
        val input = TranscoderInput(svgInputStream)
        val output = ByteArrayOutputStream()
        PNGTranscoder().transcode(input, TranscoderOutput(output))
        output.flush()
        return output.toByteArray()
    }

    @KafkaListener(topics = [LeerlingOpgehaald.TOPIC])
    fun receive(event: LeerlingOpgehaald) {
        val id = event.id
        val geslacht = event.geslacht
        println("message: ${id} ${geslacht}")

        if (id == null || geslacht == null) {
            println("ID of geslacht is null")
            return
        }
        if (leerlingRepository.findById(id).isPresent) {
            println("Leerling ${id} al eerder opgehaald")
            return
        }

        random = Random(id)

        val headers = LinkedMultiValueMap<String, String>()
        headers.add("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")

        val uri = UriComponentsBuilder.fromHttpUrl("http://avataaars.io/")
                .queryParam("avatarStyle", "Circle")
                .queryParams(params(geslacht))
                .build().toUriString();

        val response: ResponseEntity<ByteArray> = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity<ByteArray>(headers),
                ByteArray::class)
        val foto = svg2png(ByteArrayInputStream(response.body))

        leerlingRepository.save(Leerling(id, geslacht))

        eventService.send(LeerlingFotoVerkregen().apply {
            this.id = id
            this.foto = foto
        })
    }

}
