package com.bekzatsk.authservice.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
class HealthCheckController {
    @GetMapping(value = ["/ping"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Endpoint to check health of application")
    @ResponseStatus(value = HttpStatus.OK)
    fun healthCheckEndpointHandler(): ResponseEntity<*> {
        return ResponseEntity.ok(mutableMapOf("message" to "pong"))
    }
}
