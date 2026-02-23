package com.traintrack.application.dto

import com.traintrack.domain.model.exercise.Exercise

data class ExerciseDto(
    val id: Long,
    val name: String,
    val bodyPartId: Long,
    val bodyPart: BodyPartDto?,
    val isActive: Boolean
) {
    companion object {
        fun from(domain: Exercise): ExerciseDto = ExerciseDto(
            id = domain.id.value,
            name = domain.name.value,
            bodyPartId = domain.bodyPartId.value,
            bodyPart = domain.bodyPart?.let { BodyPartDto.from(it) },
            isActive = domain.isActive
        )
    }
}
