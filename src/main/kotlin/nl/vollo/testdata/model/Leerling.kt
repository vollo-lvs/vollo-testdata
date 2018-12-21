package nl.vollo.testdata.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "leerlingen")
class Leerling(
        @Id
        @Column(name = "id", updatable = false, nullable = false)
        val id: Long? = null,

        @Column
        var geslacht: String? = null
) {

    override fun toString(): String {
        return "Leerling(id=$id)"
    }

}
