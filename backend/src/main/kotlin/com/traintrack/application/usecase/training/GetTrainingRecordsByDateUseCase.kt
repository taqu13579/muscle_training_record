package com.traintrack.application.usecase.training

import com.traintrack.application.dto.TrainingRecordDto
import com.traintrack.domain.model.training.TrainingDate
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
    fun execute(date: LocalDate): List<TrainingRecordDto> {
        return trainingRecordRepository.findByTrainingDate(TrainingDate(date))
            .map { TrainingRecordDto.from(it) }
    }

    @Transactional(readOnly = true)
    fun executeByMonth(yearMonth: YearMonth): List<TrainingRecordDto> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        return trainingRecordRepository.findByTrainingDateBetween(startDate, endDate)
            .map { TrainingRecordDto.from(it) }
    }
}
