package com.traintrack.infrastructure.persistence.mapper

import com.traintrack.domain.model.exercise.Exercise
import com.traintrack.infrastructure.persistence.entity.ExerciseEntity
import org.springframework.stereotype.Component

@Component
class ExerciseMapper(
    private val bodyPartMapper: BodyPartMapper
) {
    fun toDomain(entity: ExerciseEntity): Exercise = Exercise.create(
        id = entity.id,
        name = entity.name,
        bodyPartId = entity.bodyPart.id,
        bodyPart = bodyPartMapper.toDomain(entity.bodyPart),
        isActive = entity.isActive
    )

    fun toDomainList(entities: List<ExerciseEntity>): List<Exercise> = entities.map { toDomain(it) }
}
