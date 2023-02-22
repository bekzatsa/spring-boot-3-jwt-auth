package com.bekzatsk.authservice.security.filter

import com.bekzatsk.authservice.security.CustomUserDetailService
import com.bekzatsk.authservice.security.utility.JwtUtility
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtils: JwtUtility

    @Autowired
    private lateinit var customUserDetailService: CustomUserDetailService

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                val token = authorizationHeader.substring(7)
                val userId = jwtUtils.extractUserId(token)
                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails: UserDetails = customUserDetailService.loadUserByUsername(userId.toString())
                    if (jwtUtils.validateToken(token, userDetails)) {
                        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails.username, userDetails.password, userDetails.authorities
                        )
                        usernamePasswordAuthenticationToken.details =
                            WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}
