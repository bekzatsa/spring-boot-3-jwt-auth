package com.bekzatsk.authservice.repository

import com.bekzatsk.authservice.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<User>
}
