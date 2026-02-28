package com.traintrack.application.usecase.user

import com.traintrack.application.dto.UserDto
import com.traintrack.domain.model.user.Email
import com.traintrack.domain.model.user.User
import com.traintrack.domain.model.user.Username
import com.traintrack.domain.repository.UserRepository
import com.traintrack.infrastructure.security.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class RegisterUserCommand(
    val email: String,
    val username: String,
    val password: String
)

@Service
class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun execute(command: RegisterUserCommand): UserDto {
        val email = Email(command.email)
        val username = Username(command.username)

        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("このメールアドレスは既に登録されています")
        }
        if (userRepository.existsByUsername(username)) {
            throw IllegalArgumentException("このユーザー名は既に使用されています")
        }

        validatePasswordStrength(command.password)

        val user = User.createNew(
            email = command.email,
            username = command.username,
            hashedPassword = passwordEncoder.encode(command.password)
        )

        val saved = userRepository.save(user)
        return UserDto.from(saved)
    }

    private fun validatePasswordStrength(password: String) {
        require(password.length >= 8) { "パスワードは8文字以上である必要があります" }
        require(password.any { it.isDigit() }) { "パスワードには数字を含める必要があります" }
        require(password.any { it.isLetter() }) { "パスワードには英字を含める必要があります" }
    }
}
