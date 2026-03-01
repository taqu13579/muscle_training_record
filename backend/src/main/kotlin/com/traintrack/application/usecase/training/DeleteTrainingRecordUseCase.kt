package com.traintrack.application.usecase.training

import com.traintrack.domain.model.training.TrainingRecordId
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteTrainingRecordUseCase(
    private val trainingRecordRepository: TrainingRecordRepository
) {
    @Transactional
    fun execute(userId: Long, id: Long) {
        trainingRecordRepository.deleteByIdAndUserId(TrainingRecordId(id), UserId(userId))
    }
}
