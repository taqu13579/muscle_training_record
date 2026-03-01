package com.traintrack.infrastructure.persistence.repository

import com.traintrack.domain.model.training.TrainingDate
import com.traintrack.domain.model.training.TrainingRecord
import com.traintrack.domain.model.training.TrainingRecordId
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.TrainingRecordRepository
import com.traintrack.infrastructure.persistence.entity.TrainingRecordEntity
import com.traintrack.infrastructure.persistence.mapper.TrainingRecordMapper
import com.traintrack.infrastructure.persistence.repository.jpa.JpaExerciseRepository
import com.traintrack.infrastructure.persistence.repository.jpa.JpaTrainingRecordRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
class TrainingRecordRepositoryImpl(
    private val jpaRepository: JpaTrainingRecordRepository,
    private val jpaExerciseRepository: JpaExerciseRepository,
    private val mapper: TrainingRecordMapper
) : TrainingRecordRepository {

    override fun findAllByUserId(userId: UserId, pageable: Pageable): Page<TrainingRecord> {
        return jpaRepository.findAllByUserIdWithExercise(userId.value, pageable).map { mapper.toDomain(it) }
    }

    override fun findByIdAndUserId(id: TrainingRecordId, userId: UserId): TrainingRecord? {
        return jpaRepository.findByIdAndUserIdWithExercise(id.value, userId.value)?.let { mapper.toDomain(it) }
    }

    override fun findByUserIdAndTrainingDate(userId: UserId, date: TrainingDate): List<TrainingRecord> {
        return mapper.toDomainList(jpaRepository.findByUserIdAndTrainingDateWithExercise(userId.value, date.value))
    }

    override fun findByUserIdAndTrainingDateBetween(userId: UserId, startDate: LocalDate, endDate: LocalDate): List<TrainingRecord> {
        return mapper.toDomainList(jpaRepository.findByUserIdAndTrainingDateBetweenWithExercise(userId.value, startDate, endDate))
    }

    @Transactional
    override fun save(record: TrainingRecord): TrainingRecord {
        val exerciseEntity = jpaExerciseRepository.findByIdWithBodyPart(record.exerciseId.value)
            ?: throw IllegalArgumentException("Exercise not found: ${record.exerciseId.value}")

        val entity = if (record.id.value == 0L) {
            // New record
            TrainingRecordEntity(
                userId = record.userId.value,
                exercise = exerciseEntity,
                weightKg = record.weight.value,
                repCount = record.repCount.value,
                setCount = record.setCount.value,
                trainingDate = record.trainingDate.value,
                memo = record.memo
            )
        } else {
            // Update existing
            val existing = jpaRepository.findByIdAndUserIdWithExercise(record.id.value, record.userId.value)
                ?: throw IllegalArgumentException("TrainingRecord not found: ${record.id.value}")
            existing.weightKg = record.weight.value
            existing.repCount = record.repCount.value
            existing.setCount = record.setCount.value
            existing.trainingDate = record.trainingDate.value
            existing.memo = record.memo
            existing
        }

        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    @Transactional
    override fun deleteByIdAndUserId(id: TrainingRecordId, userId: UserId) {
        jpaRepository.deleteByIdAndUserId(id.value, userId.value)
    }
}
