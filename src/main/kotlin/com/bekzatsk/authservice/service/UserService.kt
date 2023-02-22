package com.bekzatsk.authservice.service

import com.bekzatsk.authservice.dto.UserCreateRequestDto
import com.bekzatsk.authservice.dto.UserDetailDto
import com.bekzatsk.authservice.dto.UserUpdateRequestDto
import com.bekzatsk.authservice.dto.mapper.UserPatchOperationMapper
import com.bekzatsk.authservice.model.User
import com.bekzatsk.authservice.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
@RequiredArgsConstructor
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun create(userCreateRequestDto: UserCreateRequestDto) {
        if (emailAlreadyTaken(userCreateRequestDto.email!!)) throw ResponseStatusException(
            HttpStatus.CONFLICT,
            "Account with provided email-id already exists"
        )
        val user = User()
        user.firstName = userCreateRequestDto.firstName
        user.lastName = userCreateRequestDto.lastName
        user.email = userCreateRequestDto.email
        user.password = passwordEncoder.encode(userCreateRequestDto.password)
        userRepository.save(user)
    }

    fun update(userId: UUID, userUpdateRequestDto: UserUpdateRequestDto) {
        val user: User = getUserById(userId)
        Mappers.getMapper(UserPatchOperationMapper::class.java).patch(userUpdateRequestDto, user)
        userRepository.save(user)
    }

    fun getById(userId: UUID): UserDetailDto {
        val user: User = getUserById(userId)
        return UserDetailDto(firstName = user.firstName, lastName = user.lastName, email = user.email, createdAt = user.createdAt.toLocalDateTime())
    }

    private fun getUserById(userId: UUID): User {
        return userRepository.findById(userId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user-id provided") }
    }

    private fun emailAlreadyTaken(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}
