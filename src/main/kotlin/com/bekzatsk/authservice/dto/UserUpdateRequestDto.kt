package com.bekzatsk.authservice.dto

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode
import lombok.extern.jackson.Jacksonized

@Jacksonized
data class UserUpdateRequestDto(
    @Schema(
        requiredMode = RequiredMode.NOT_REQUIRED,
        description = "first-name of user",
        example = "Hardik",
        maxLength = 15,
        minLength = 3
    )
    private val firstName: String? = null,

    @Schema(
        requiredMode = RequiredMode.NOT_REQUIRED,
        description = "last-name of user",
        example = "Singh",
        maxLength = 15,
        minLength = 3
    )
    private val lastName: String? = null
)
