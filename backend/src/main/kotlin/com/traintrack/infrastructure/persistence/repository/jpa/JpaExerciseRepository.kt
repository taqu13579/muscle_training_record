package com.traintrack.infrastructure.persistence.repository.jpa

import com.traintrack.infrastructure.persistence.entity.ExerciseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JpaExerciseRepository : JpaRepository<ExerciseEntity, Long> {
    fun findByBodyPartId(bodyPartId: Long): List<ExerciseEntity>
    fun findByIsActiveTrue(): List<ExerciseEntity>

    @Query("SELECT e FROM ExerciseEntity e JOIN FETCH e.bodyPart")
    fun findAllWithBodyPart(): List<ExerciseEntity>

    @Query("SELECT e FROM ExerciseEntity e JOIN FETCH e.bodyPart WHERE e.id = :id")
    fun findByIdWithBodyPart(id: Long): ExerciseEntity?
}
