package com.traintrack.application.usecase.training

import com.traintrack.application.dto.TrainingRecordDto
import com.traintrack.domain.model.training.TrainingRecordId
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetTrainingRecordByIdUseCase(
    private val trainingRecordRepository: TrainingRecordRepository
) {
    @Transactional(readOnly = true)
    fun execute(userId: Long, id: Long): TrainingRecordDto? {
        return trainingRecordRepository.findByIdAndUserId(TrainingRecordId(id), UserId(userId))?.let { TrainingRecordDto.from(it) }
    }
}
