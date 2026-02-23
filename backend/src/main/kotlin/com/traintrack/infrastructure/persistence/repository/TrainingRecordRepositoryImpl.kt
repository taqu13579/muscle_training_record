package com.traintrack.infrastructure.persistence.repository

import com.traintrack.domain.model.training.TrainingDate
import com.traintrack.domain.model.training.TrainingRecord
import com.traintrack.domain.model.training.TrainingRecordId
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

    override fun findAll(pageable: Pageable): Page<TrainingRecord> {
        return jpaRepository.findAllWithExercise(pageable).map { mapper.toDomain(it) }
    }

    override fun findById(id: TrainingRecordId): TrainingRecord? {
        return jpaRepository.findByIdWithExercise(id.value)?.let { mapper.toDomain(it) }
    }

    override fun findByTrainingDate(date: TrainingDate): List<TrainingRecord> {
        return mapper.toDomainList(jpaRepository.findByTrainingDateWithExercise(date.value))
    }

    override fun findByTrainingDateBetween(startDate: LocalDate, endDate: LocalDate): List<TrainingRecord> {
        return mapper.toDomainList(jpaRepository.findByTrainingDateBetweenWithExercise(startDate, endDate))
    }

    @Transactional
    override fun save(record: TrainingRecord): TrainingRecord {
        val exerciseEntity = jpaExerciseRepository.findByIdWithBodyPart(record.exerciseId.value)
            ?: throw IllegalArgumentException("Exercise not found: ${record.exerciseId.value}")

        val entity = if (record.id.value == 1L && jpaRepository.findById(1L).isEmpty) {
            // New record
            TrainingRecordEntity(
                exercise = exerciseEntity,
                weightKg = record.weight.value,
                repCount = record.repCount.value,
                setCount = record.setCount.value,
                trainingDate = record.trainingDate.value,
                memo = record.memo
            )
        } else {
            // Update existing
            val existing = jpaRepository.findById(record.id.value)
                .orElseThrow { IllegalArgumentException("TrainingRecord not found: ${record.id.value}") }
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
    override fun deleteById(id: TrainingRecordId) {
        jpaRepository.deleteById(id.value)
    }
}
