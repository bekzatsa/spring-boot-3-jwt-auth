package com.bekzatsk.authservice.security.utility

import com.bekzatsk.authservice.configuration.properties.JwtConfigurationProperties
import com.bekzatsk.authservice.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClaims
import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Function

@Component
@EnableConfigurationProperties(JwtConfigurationProperties::class)
@RequiredArgsConstructor
class JwtUtility {

    @Autowired
    private lateinit var jwtConfigurationProperties: JwtConfigurationProperties

    @Autowired
    private lateinit var httpRequest: HttpServletRequest

    @Value("\${spring.application.name}")
    private val issuer: String? = null
    fun extractUserId(token: String): UUID {
        return UUID.fromString(extractAllClaims(token)["user_id"] as String?)
    }

    fun extractExpirationTimestamp(token: String): LocalDateTime {
        return extractClaim(token) { obj: Claims -> obj.expiration }.toInstant().atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneId.of("+00:00")).toLocalDateTime()
    }

    fun generateAccessToken(user: User): String {
        val claims: Claims = DefaultClaims()
        claims["user_id"] = user.id
        claims["account_creation_timestamp"] =
            user.createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        claims["name"] = user.firstName + " " + user.lastName
        println("validity ${jwtConfigurationProperties.jwt.accessToken.validity}")
        return createToken(
            claims, user.email!!,
            TimeUnit.MINUTES.toMillis(jwtConfigurationProperties.jwt.accessToken.validity)
        )
    }

    fun generateRefreshToken(user: User): String {
        val claims: Claims = DefaultClaims()
        claims["user_id"] = user.id
        return createToken(
            claims, user.email!!,
            TimeUnit.DAYS.toMillis(jwtConfigurationProperties.jwt.refreshToken.validity)
        )
    }

    fun validateToken(token: String, user: UserDetails): Boolean {
        val email = extractClaim(
            token
        ) { obj: Claims -> obj.subject }
        return email == user.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean {
        val tokenExpirationDate = extractClaim(
            token
        ) { obj: Claims -> obj.expiration }
        return tokenExpirationDate.before(Date())
    }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(jwtConfigurationProperties.jwt.secretKey)
            .parseClaimsJws(token.replace("Bearer ", "")).body
    }

    private fun createToken(claims: Claims, subject: String, expiration: Long): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration)).setId(UUID.randomUUID().toString())
            .setAudience(httpRequest!!.remoteHost).setIssuer(issuer)
            .signWith(SignatureAlgorithm.HS256, jwtConfigurationProperties.jwt.secretKey).compact()
    }
}
