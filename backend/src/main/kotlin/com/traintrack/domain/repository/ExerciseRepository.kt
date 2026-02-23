package com.traintrack.domain.repository

import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.model.exercise.Exercise
import com.traintrack.domain.model.exercise.ExerciseId

interface ExerciseRepository {
    fun findAll(): List<Exercise>
    fun findById(id: ExerciseId): Exercise?
    fun findByBodyPartId(bodyPartId: BodyPartId): List<Exercise>
    fun findActiveExercises(): List<Exercise>
    fun save(exercise: Exercise): Exercise
    fun deleteById(id: ExerciseId)
}
