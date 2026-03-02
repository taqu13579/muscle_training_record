package com.traintrack.application.usecase.bodypart

import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.repository.BodyPartRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteBodyPartUseCase(
    private val bodyPartRepository: BodyPartRepository
) {
    @Transactional
    fun execute(id: Long) {
        bodyPartRepository.deleteById(BodyPartId(id))
    }
}
