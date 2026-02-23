package com.traintrack.domain.repository

import com.traintrack.domain.model.training.TrainingDate
import com.traintrack.domain.model.training.TrainingRecord
import com.traintrack.domain.model.training.TrainingRecordId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface TrainingRecordRepository {
    fun findAll(pageable: Pageable): Page<TrainingRecord>
    fun findById(id: TrainingRecordId): TrainingRecord?
    fun findByTrainingDate(date: TrainingDate): List<TrainingRecord>
    fun findByTrainingDateBetween(startDate: LocalDate, endDate: LocalDate): List<TrainingRecord>
    fun save(record: TrainingRecord): TrainingRecord
    fun deleteById(id: TrainingRecordId)
}
