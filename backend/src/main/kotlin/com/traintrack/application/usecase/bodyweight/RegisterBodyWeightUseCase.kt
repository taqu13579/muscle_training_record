package com.traintrack.application.usecase.bodyweight

import com.traintrack.application.dto.BodyWeightDto
import com.traintrack.domain.model.bodyweight.BodyWeight
import com.traintrack.domain.repository.BodyWeightRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

data class RegisterBodyWeightCommand(
    val userId: Long,
    val weightKg: BigDecimal,
    val recordedDate: LocalDate,
    val memo: String?
)

@Service
class RegisterBodyWeightUseCase(
    private val bodyWeightRepository: BodyWeightRepository
) {
    @Transactional
    fun execute(command: RegisterBodyWeightCommand): BodyWeightDto {
        val bodyWeight = BodyWeight.createNew(
            userId = command.userId,
            weightKg = command.weightKg,
            recordedDate = command.recordedDate,
            memo = command.memo
        )
        val saved = bodyWeightRepository.save(bodyWeight)
        return BodyWeightDto.from(saved)
    }
}
