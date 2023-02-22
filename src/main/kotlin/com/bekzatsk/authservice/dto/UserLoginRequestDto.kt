package com.bekzatsk.authservice.dto

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import lombok.extern.jackson.Jacksonized

@Jacksonized
data class UserLoginRequestDto(
    @Schema(
        requiredMode = RequiredMode.REQUIRED,
        name = "email",
        example = "bekzat.saylaubay@gmail.com",
        description = "email-id associated with user account already created in the system"
    )
    val email: @NotBlank(message = "email-id must not be empty") @Email(message = "email-id must be of valid format") String? = null,

    @Schema(
        requiredMode = RequiredMode.REQUIRED,
        example = "somethingSecure",
        description = "password corresponding to provided email-id"
    )
    val password: @NotBlank(message = "password must not be empty") String? = null
)
