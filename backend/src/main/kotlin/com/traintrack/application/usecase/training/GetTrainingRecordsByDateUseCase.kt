package com.traintrack.application.usecase.training

import com.traintrack.application.dto.TrainingRecordDto
import com.traintrack.domain.model.training.TrainingDate
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.YearMonth

@Service
class GetTrainingRecordsByDateUseCase(
    private val trainingRecordRepository: TrainingRecordRepository
) {
    @Transactional(readOnly = true)
    fun execute(userId: Long, date: LocalDate): List<TrainingRecordDto> {
        return trainingRecordRepository.findByUserIdAndTrainingDate(UserId(userId), TrainingDate(date))
            .map { TrainingRecordDto.from(it) }
    }

    @Transactional(readOnly = true)
    fun executeByMonth(userId: Long, yearMonth: YearMonth): List<TrainingRecordDto> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        return trainingRecordRepository.findByUserIdAndTrainingDateBetween(UserId(userId), startDate, endDate)
            .map { TrainingRecordDto.from(it) }
    }
}
