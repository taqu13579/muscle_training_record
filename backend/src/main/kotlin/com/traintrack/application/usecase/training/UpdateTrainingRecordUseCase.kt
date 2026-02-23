package com.traintrack.application.usecase.training

import com.traintrack.application.dto.TrainingRecordDto
import com.traintrack.domain.model.training.TrainingRecord
import com.traintrack.domain.model.training.TrainingRecordId
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

data class UpdateTrainingRecordCommand(
    val id: Long,
    val exerciseId: Long,
    val weightKg: BigDecimal,
    val repCount: Int,
    val setCount: Int,
    val trainingDate: LocalDate,
    val memo: String?
)

@Service
class UpdateTrainingRecordUseCase(
    private val trainingRecordRepository: TrainingRecordRepository
) {
    @Transactional
    fun execute(command: UpdateTrainingRecordCommand): TrainingRecordDto {
        val existing = trainingRecordRepository.findById(TrainingRecordId(command.id))
            ?: throw IllegalArgumentException("TrainingRecord not found: ${command.id}")

        val updated = TrainingRecord.create(
            id = command.id,
            exerciseId = command.exerciseId,
            exercise = existing.exercise,
            weightKg = command.weightKg,
            repCount = command.repCount,
            setCount = command.setCount,
            trainingDate = command.trainingDate,
            memo = command.memo,
            createdAt = existing.createdAt,
            updatedAt = existing.updatedAt
        )

        val saved = trainingRecordRepository.save(updated)
        return TrainingRecordDto.from(saved)
    }
}
