package com.traintrack.application.usecase.user

import com.traintrack.application.dto.UserDto
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.model.user.UserRole
import com.traintrack.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class UpdateUserRoleCommand(
    val userId: Long,
    val role: String
)

@Service
class UpdateUserRoleUseCase(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(command: UpdateUserRoleCommand): UserDto {
        val role = runCatching { UserRole.valueOf(command.role) }
            .getOrElse { throw IllegalArgumentException("無効なロールです: ${command.role}") }

        val user = userRepository.findById(UserId(command.userId))
            ?: throw IllegalArgumentException("ユーザーが見つかりません: ${command.userId}")

        val updated = user.copy(role = role)
        val saved = userRepository.save(updated)
        return UserDto.from(saved)
    }
}
