package nl.vollo.testdata.repository

import nl.vollo.testdata.model.Leerling
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeerlingRepository : JpaRepository<Leerling, Long>