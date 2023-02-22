package com.bekzatsk.authservice.controller

import com.bekzatsk.authservice.dto.UserCreateRequestDto
import com.bekzatsk.authservice.dto.UserDetailDto
import com.bekzatsk.authservice.dto.UserUpdateRequestDto
import com.bekzatsk.authservice.security.utility.JwtUtility
import com.bekzatsk.authservice.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/users"])
@RequiredArgsConstructor
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtility: JwtUtility

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Creates a user account",
        description = "Registers a unique user record in the system corresponding to the provided information"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "User account created successfully"
        ), ApiResponse(responseCode = "409", description = "User account with provided email-id already exists")]
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    fun userCreationHandler(
        @RequestBody(required = true) userCreateRequestDto: @Valid UserCreateRequestDto
    ): ResponseEntity<HttpStatus> {
        userService.create(userCreateRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Updates user account details", description = "Updates account details for the logged-in user")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "User account details updated successfully"
        )]
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun userAccountDetailsUpdateHandler(
        @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = true) accessToken: String,
        @RequestBody(required = true) userUpdateRequestDto: @Valid UserUpdateRequestDto
    ): ResponseEntity<HttpStatus> {
        userService.update(jwtUtility.extractUserId(accessToken), userUpdateRequestDto)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Retrieves current logged-in user's account details",
        description = "Private endpoint which retreives user account details against the Access-token JWT provided in headers"
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun loggedInUserDetailRetreivalHandler(
        @Parameter(hidden = true) @RequestHeader(name = "Authorization", required = true) accessToken: String
    ): ResponseEntity<UserDetailDto> {
        return ResponseEntity.ok(userService.getById(jwtUtility.extractUserId(accessToken)))
    }
}
