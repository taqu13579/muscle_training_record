package com.traintrack.application.usecase.user

import com.traintrack.application.dto.UserDto
import com.traintrack.domain.exception.AuthenticationException
import com.traintrack.domain.model.user.Email
import com.traintrack.domain.repository.UserRepository
import com.traintrack.infrastructure.security.JwtProvider
import com.traintrack.infrastructure.security.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class LoginUserCommand(
    val email: String,
    val password: String
)

data class LoginResult(
    val user: UserDto,
    val accessToken: String
)

@Service
class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {
    @Transactional(readOnly = true)
    fun execute(command: LoginUserCommand): LoginResult {
        val email = Email(command.email)

        val user = userRepository.findByEmail(email)
            ?: throw AuthenticationException.InvalidCredentials()

        if (!passwordEncoder.matches(command.password, user.hashedPassword.value)) {
            throw AuthenticationException.InvalidCredentials()
        }

        val accessToken = jwtProvider.generateToken(user.id.value, user.email.value)

        return LoginResult(
            user = UserDto.from(user),
            accessToken = accessToken
        )
    }
}
