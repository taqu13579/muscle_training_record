package com.traintrack.application.usecase.bodypart

import com.traintrack.application.dto.BodyPartDto
import com.traintrack.domain.repository.BodyPartRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetBodyPartsUseCase(
    private val bodyPartRepository: BodyPartRepository
) {
    @Transactional(readOnly = true)
    fun execute(): List<BodyPartDto> {
        return bodyPartRepository.findAll().map { BodyPartDto.from(it) }
    }
}
