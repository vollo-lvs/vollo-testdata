package nl.vollo.testdata.events

import nl.vollo.testdata.model.Leerling

class LeerlingFotoVerkregen(override val body: Leerling) : Event<Leerling> {
    override val name = "LeerlingFotoVerkregen"
}