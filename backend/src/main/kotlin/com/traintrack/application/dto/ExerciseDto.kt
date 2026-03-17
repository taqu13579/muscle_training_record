package com.traintrack.application.dto

import com.traintrack.domain.model.exercise.Exercise

data class ExerciseDto(
    val id: Long,
    val name: String,
    val bodyPartId: Long,
    val bodyPart: BodyPartDto?,
    val isActive: Boolean,
    val description: String? = null,
    val auxiliaryMuscles: List<BodyPartDto> = emptyList()
) {
    companion object {
        fun from(domain: Exercise): ExerciseDto = ExerciseDto(
            id = domain.id.value,
            name = domain.name.value,
            bodyPartId = domain.bodyPartId.value,
            bodyPart = domain.bodyPart?.let { BodyPartDto.from(it) },
            isActive = domain.isActive,
            description = domain.description,
            auxiliaryMuscles = domain.auxiliaryMuscles.map { BodyPartDto.from(it) }
        )
    }
}
