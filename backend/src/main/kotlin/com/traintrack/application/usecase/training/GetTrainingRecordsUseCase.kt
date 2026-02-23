package com.traintrack.application.usecase.training

import com.traintrack.application.dto.TrainingRecordDto
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetTrainingRecordsUseCase(
    private val trainingRecordRepository: TrainingRecordRepository
) {
    @Transactional(readOnly = true)
    fun execute(pageable: Pageable): Page<TrainingRecordDto> {
        return trainingRecordRepository.findAll(pageable).map { TrainingRecordDto.from(it) }
    }
}
