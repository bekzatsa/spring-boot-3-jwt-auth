package com.bekzatsk.authservice.dto

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import lombok.extern.jackson.Jacksonized

@Jacksonized
data class UserCreateRequestDto(
    @Schema(
        requiredMode = RequiredMode.REQUIRED,
        description = "first-name of user",
        example = "Bekzat",
        maxLength = 15,
        minLength = 3
    )
    val firstName: @NotBlank(message = "first-name must not be empty") String? = null,

    @Schema(
        requiredMode = RequiredMode.NOT_REQUIRED,
        description = "last-name of user",
        example = "Sailaubayev",
        maxLength = 15,
        minLength = 3
    )
    val lastName: String? = null,

    @Schema(
        requiredMode = RequiredMode.REQUIRED,
        description = "email-id of user",
        example = "bekzat.saylaubay@gmail.com"
    )
    val email: @NotBlank(message = "email-id must not be empty") @Email(message = "email-id must be of valid format") String? = null,

    @Schema(
        requiredMode = RequiredMode.REQUIRED,
        description = "secure password to enable user login",
        example = "somethingSecure"
    )
    val password: @NotBlank(message = "password must not be empty") String? = null
)
