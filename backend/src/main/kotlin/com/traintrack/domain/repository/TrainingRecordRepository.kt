package com.traintrack.domain.repository

import com.traintrack.domain.model.training.TrainingDate
import com.traintrack.domain.model.training.TrainingRecord
import com.traintrack.domain.model.training.TrainingRecordId
import com.traintrack.domain.model.user.UserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface TrainingRecordRepository {
    fun findAllByUserId(userId: UserId, pageable: Pageable): Page<TrainingRecord>
    fun findByIdAndUserId(id: TrainingRecordId, userId: UserId): TrainingRecord?
    fun findByUserIdAndTrainingDate(userId: UserId, date: TrainingDate): List<TrainingRecord>
    fun findByUserIdAndTrainingDateBetween(userId: UserId, startDate: LocalDate, endDate: LocalDate): List<TrainingRecord>
    fun save(record: TrainingRecord): TrainingRecord
    fun deleteByIdAndUserId(id: TrainingRecordId, userId: UserId)
}
