package com.traintrack.infrastructure.persistence.repository

import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.model.exercise.Exercise
import com.traintrack.domain.model.exercise.ExerciseId
import com.traintrack.domain.repository.ExerciseRepository
import com.traintrack.infrastructure.persistence.entity.ExerciseEntity
import com.traintrack.infrastructure.persistence.mapper.ExerciseMapper
import com.traintrack.infrastructure.persistence.repository.jpa.JpaBodyPartRepository
import com.traintrack.infrastructure.persistence.repository.jpa.JpaExerciseRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ExerciseRepositoryImpl(
    private val jpaRepository: JpaExerciseRepository,
    private val jpaBodyPartRepository: JpaBodyPartRepository,
    private val mapper: ExerciseMapper
) : ExerciseRepository {

    override fun findAll(): List<Exercise> {
        return mapper.toDomainList(jpaRepository.findAllWithBodyPart())
    }

    override fun findById(id: ExerciseId): Exercise? {
        return jpaRepository.findByIdWithBodyPart(id.value)?.let { mapper.toDomain(it) }
    }

    override fun findByBodyPartId(bodyPartId: BodyPartId): List<Exercise> {
        return mapper.toDomainList(jpaRepository.findByBodyPartId(bodyPartId.value))
    }

    override fun findActiveExercises(): List<Exercise> {
        return mapper.toDomainList(jpaRepository.findByIsActiveTrue())
    }

    @Transactional
    override fun save(exercise: Exercise): Exercise {
        val bodyPartEntity = jpaBodyPartRepository.findById(exercise.bodyPartId.value)
            .orElseThrow { IllegalArgumentException("BodyPart not found: ${exercise.bodyPartId.value}") }

        val entity = if (exercise.id.value == 0L) {
            // New exercise
            ExerciseEntity(
                name = exercise.name.value,
                bodyPart = bodyPartEntity,
                isActive = exercise.isActive
            )
        } else {
            // Update existing
            val existing = jpaRepository.findById(exercise.id.value)
                .orElseThrow { IllegalArgumentException("Exercise not found: ${exercise.id.value}") }
            existing.name = exercise.name.value
            existing.isActive = exercise.isActive
            existing
        }

        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    @Transactional
    override fun deleteById(id: ExerciseId) {
        val entity = jpaRepository.findById(id.value)
            .orElseThrow { IllegalArgumentException("Exercise not found: ${id.value}") }
        entity.isActive = false
        jpaRepository.save(entity)
    }
}
