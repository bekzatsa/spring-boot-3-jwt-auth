package com.bekzatsk.authservice.exception.dto

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JacksonStdImpl
data class ExceptionResponseDto(
    val status: String = "Failure",
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(),
    val message: String
)
