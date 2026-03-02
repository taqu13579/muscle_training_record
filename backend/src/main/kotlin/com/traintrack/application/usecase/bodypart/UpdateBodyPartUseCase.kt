package com.traintrack.application.usecase.bodypart

import com.traintrack.application.dto.BodyPartDto
import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.model.bodypart.BodyPartName
import com.traintrack.domain.repository.BodyPartRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class UpdateBodyPartCommand(
    val id: Long,
    val name: String,
    val displayOrder: Int
)

@Service
class UpdateBodyPartUseCase(
    private val bodyPartRepository: BodyPartRepository
) {
    @Transactional
    fun execute(command: UpdateBodyPartCommand): BodyPartDto {
        val bodyPart = bodyPartRepository.findById(BodyPartId(command.id))
            ?: throw IllegalArgumentException("部位が見つかりません: ${command.id}")

        val updated = bodyPart.copy(
            name = BodyPartName(command.name),
            displayOrder = command.displayOrder
        )
        val saved = bodyPartRepository.update(updated)
        return BodyPartDto.from(saved)
    }
}
