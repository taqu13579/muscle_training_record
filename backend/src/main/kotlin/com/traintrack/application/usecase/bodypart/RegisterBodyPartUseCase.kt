package com.traintrack.application.usecase.bodypart

import com.traintrack.application.dto.BodyPartDto
import com.traintrack.domain.repository.BodyPartRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class RegisterBodyPartCommand(
    val name: String,
    val displayOrder: Int
)

@Service
class RegisterBodyPartUseCase(
    private val bodyPartRepository: BodyPartRepository
) {
    @Transactional
    fun execute(command: RegisterBodyPartCommand): BodyPartDto {
        if (bodyPartRepository.existsByName(command.name)) {
            throw IllegalArgumentException("この部位名は既に登録されています: ${command.name}")
        }
        val created = bodyPartRepository.create(command.name, command.displayOrder)
        return BodyPartDto.from(created)
    }
}
