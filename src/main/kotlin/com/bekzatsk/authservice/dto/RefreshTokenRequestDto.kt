package com.bekzatsk.authservice.dto

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode
import jakarta.validation.constraints.NotBlank
import lombok.extern.jackson.Jacksonized

@Jacksonized
data class RefreshTokenRequestDto(
    @Schema(requiredMode = RequiredMode.REQUIRED, description = "refresh-token received during successful login")
    val refreshToken: @NotBlank(message = "Refresh token must not be empty") String? = null
)
