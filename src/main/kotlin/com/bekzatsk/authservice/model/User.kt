package com.bekzatsk.authservice.model

import jakarta.persistence.*
import java.util.*

@Entity(name = "USERS")
@Table
class User: Auditable<String?>() {
    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(nullable = false)
    var firstName: String? = null

    @Column(nullable = true)
    var lastName: String? = null

    @Column(nullable = false, unique = true)
    var email: String? = null

    @Column(nullable = false)
    var password: String? = null
}
