package com.traintrack.infrastructure.persistence.mapper

import com.traintrack.domain.model.training.TrainingRecord
import com.traintrack.infrastructure.persistence.entity.TrainingRecordEntity
import org.springframework.stereotype.Component

@Component
class TrainingRecordMapper(
    private val exerciseMapper: ExerciseMapper
) {
    fun toDomain(entity: TrainingRecordEntity): TrainingRecord = TrainingRecord.create(
        id = entity.id,
        exerciseId = entity.exercise.id,
        exercise = exerciseMapper.toDomain(entity.exercise),
        weightKg = entity.weightKg,
        repCount = entity.repCount,
        setCount = entity.setCount,
        trainingDate = entity.trainingDate,
        memo = entity.memo,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toDomainList(entities: List<TrainingRecordEntity>): List<TrainingRecord> = entities.map { toDomain(it) }
}
