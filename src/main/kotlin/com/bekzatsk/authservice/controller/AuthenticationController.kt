package com.bekzatsk.authservice.controller

import com.bekzatsk.authservice.dto.RefreshTokenRequestDto
import com.bekzatsk.authservice.dto.TokenSuccessResponseDto
import com.bekzatsk.authservice.dto.UserLoginRequestDto
import com.bekzatsk.authservice.service.AuthenticationService
import io.swagger.v3.oas.annotations.Operation
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
@RequestMapping(value = ["/auth"])
@RequiredArgsConstructor
class AuthenticationController {
    @Autowired
    private lateinit var authenticationService: AuthenticationService
    @PostMapping(
        value = ["/login"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Logs in user into the system",
        description = "Returns Access-token and Refresh-token on successfull authentication which provides access to protected endpoints"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Authentication successfull"
        ), ApiResponse(
            responseCode = "401",
            description = "Bad credentials provided. Failed to authenticate user"
        )]
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun userLoginRequestHandler(
        @RequestBody(required = true) userLoginRequestDto: @Valid UserLoginRequestDto
    ): ResponseEntity<TokenSuccessResponseDto> {
        return ResponseEntity.ok(authenticationService.login(userLoginRequestDto))
    }

    @PutMapping(
        value = ["/refresh"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Refreshes Access-Token for a user",
        description = "Provides a new Access-token against the user for which the non expired refresh-token is provided"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Access-token refreshed"
        ), ApiResponse(
            responseCode = "403",
            description = "Refresh token has expired. Failed to refresh access token"
        )]
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun accessTokenRefreshalRequestHandler(
        @RequestBody(required = true) refreshTokenRequestDto: @Valid RefreshTokenRequestDto
    ): ResponseEntity<TokenSuccessResponseDto> {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequestDto))
    }
}
