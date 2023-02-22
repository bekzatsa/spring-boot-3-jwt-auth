package com.bekzatsk.authservice.service

import com.bekzatsk.authservice.dto.RefreshTokenRequestDto
import com.bekzatsk.authservice.dto.TokenSuccessResponseDto
import com.bekzatsk.authservice.dto.UserLoginRequestDto
import com.bekzatsk.authservice.repository.UserRepository
import com.bekzatsk.authservice.security.utility.JwtUtility
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
@RequiredArgsConstructor
class AuthenticationService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var jwtUtility: JwtUtility

    fun login(userLoginRequestDto: UserLoginRequestDto): TokenSuccessResponseDto {
        val user = userRepository.findByEmail(userLoginRequestDto.email!!)
            .orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login credentials provided") }
        if (!passwordEncoder.matches(
                userLoginRequestDto.password,
                user.password
            )
        ) throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login credentials provided")
        val accessToken = jwtUtility.generateAccessToken(user)
        val refreshToken = jwtUtility.generateRefreshToken(user)
        val accessTokenExpirationTimestamp = jwtUtility.extractExpirationTimestamp(accessToken)
        return TokenSuccessResponseDto(accessToken = accessToken, refreshToken = refreshToken, expiresAt = accessTokenExpirationTimestamp)
    }

    fun refreshToken(refreshTokenRequestDto: RefreshTokenRequestDto): TokenSuccessResponseDto {
        if (jwtUtility.isTokenExpired(refreshTokenRequestDto.refreshToken!!)) throw ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "Token expired"
        )
        val userId: UUID = jwtUtility.extractUserId(refreshTokenRequestDto.refreshToken)
        val user = userRepository.findById(userId)
            .orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user-id provided") }
        val accessToken = jwtUtility.generateAccessToken(user)
        val accessTokenExpirationTimestamp = jwtUtility.extractExpirationTimestamp(accessToken)
        return TokenSuccessResponseDto(accessToken = accessToken, expiresAt = accessTokenExpirationTimestamp)
    }
}
