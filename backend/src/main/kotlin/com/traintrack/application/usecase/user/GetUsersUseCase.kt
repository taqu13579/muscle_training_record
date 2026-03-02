package com.traintrack.application.usecase.user

import com.traintrack.application.dto.UserDto
import com.traintrack.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun execute(): List<UserDto> {
        return userRepository.findAll().map { UserDto.from(it) }
    }
}
