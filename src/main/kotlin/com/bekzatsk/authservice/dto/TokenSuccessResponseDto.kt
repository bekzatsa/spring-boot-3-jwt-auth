package com.bekzatsk.authservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import lombok.extern.jackson.Jacksonized
import java.time.LocalDateTime

@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenSuccessResponseDto(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val expiresAt: LocalDateTime? = null
)
