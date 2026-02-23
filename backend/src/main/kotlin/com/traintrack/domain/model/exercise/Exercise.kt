package com.traintrack.domain.model.exercise

import com.traintrack.domain.model.bodypart.BodyPart
import com.traintrack.domain.model.bodypart.BodyPartId

data class Exercise(
    val id: ExerciseId,
    val name: ExerciseName,
    val bodyPartId: BodyPartId,
    val bodyPart: BodyPart? = null,
    val isActive: Boolean = true
) {
    companion object {
        fun create(
            id: Long,
            name: String,
            bodyPartId: Long,
            bodyPart: BodyPart? = null,
            isActive: Boolean = true
        ): Exercise = Exercise(
            id = ExerciseId(id),
            name = ExerciseName(name),
            bodyPartId = BodyPartId(bodyPartId),
            bodyPart = bodyPart,
            isActive = isActive
        )

        fun createNew(
            name: String,
            bodyPartId: Long
        ): Exercise = Exercise(
            id = ExerciseId(1), // Placeholder, will be assigned by DB
            name = ExerciseName(name),
            bodyPartId = BodyPartId(bodyPartId),
            isActive = true
        )
    }

    fun deactivate(): Exercise = copy(isActive = false)

    fun activate(): Exercise = copy(isActive = true)

    fun updateName(newName: String): Exercise = copy(name = ExerciseName(newName))
}
