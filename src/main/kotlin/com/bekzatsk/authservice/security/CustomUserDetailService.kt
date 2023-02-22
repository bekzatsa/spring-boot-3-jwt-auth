package com.bekzatsk.authservice.security

import com.bekzatsk.authservice.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

@Configuration
@RequiredArgsConstructor
class CustomUserDetailService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(userId: String): UserDetails {
        return convert(userRepository.findById(UUID.fromString(userId))
            .orElseThrow { UsernameNotFoundException("Bad Credentials") })
    }

    private fun convert(user: com.bekzatsk.authservice.model.User): User {
        return User(user.email, user.password, listOf())
    }
}
