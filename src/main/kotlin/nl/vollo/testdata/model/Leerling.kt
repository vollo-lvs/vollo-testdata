package nl.vollo.testdata.model

class Leerling(val id: Long, val geslacht: String) {
    val _type = "LEERLING"
    var foto: ByteArray? = null

    override fun toString(): String {
        return "Leerling(id=$id)"
    }

}
