package com.traintrack.application.usecase.training

import com.traintrack.application.dto.TrainingRecordDto
import com.traintrack.domain.model.training.TrainingRecord
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

data class RegisterTrainingRecordCommand(
    val userId: Long,
    val exerciseId: Long,
    val weightKg: BigDecimal,
    val repCount: Int,
    val setCount: Int,
    val trainingDate: LocalDate,
    val memo: String?
)

@Service
class RegisterTrainingRecordUseCase(
    private val trainingRecordRepository: TrainingRecordRepository
) {
    @Transactional
    fun execute(command: RegisterTrainingRecordCommand): TrainingRecordDto {
        val record = TrainingRecord.createNew(
            userId = command.userId,
            exerciseId = command.exerciseId,
            weightKg = command.weightKg,
            repCount = command.repCount,
            setCount = command.setCount,
            trainingDate = command.trainingDate,
            memo = command.memo
        )
        val saved = trainingRecordRepository.save(record)
        return TrainingRecordDto.from(saved)
    }
}
