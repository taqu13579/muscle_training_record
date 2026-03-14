package com.traintrack.application.usecase.bodyweight

import com.traintrack.application.dto.BodyWeightDto
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.BodyWeightRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GetBodyWeightsUseCase(
    private val bodyWeightRepository: BodyWeightRepository
) {
    fun execute(userId: Long): List<BodyWeightDto> {
        return bodyWeightRepository.findAllByUserId(UserId(userId)).map { BodyWeightDto.from(it) }
    }

    fun executeByDateRange(userId: Long, startDate: LocalDate, endDate: LocalDate): List<BodyWeightDto> {
        return bodyWeightRepository.findByUserIdAndDateBetween(UserId(userId), startDate, endDate)
            .map { BodyWeightDto.from(it) }
    }
}
