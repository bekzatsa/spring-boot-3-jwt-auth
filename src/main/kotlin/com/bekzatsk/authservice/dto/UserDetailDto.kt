package com.bekzatsk.authservice.dto

import lombok.extern.jackson.Jacksonized
import java.time.LocalDateTime

@Jacksonized
data class UserDetailDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val createdAt: LocalDateTime? = null
)
