package com.traintrack.presentation.response

import com.traintrack.application.dto.ExerciseDto

data class ExerciseResponse(
    val id: Long,
    val name: String,
    val bodyPart: BodyPartResponse?,
    val isActive: Boolean
) {
    companion object {
        fun from(dto: ExerciseDto): ExerciseResponse = ExerciseResponse(
            id = dto.id,
            name = dto.name,
            bodyPart = dto.bodyPart?.let { BodyPartResponse.from(it) },
            isActive = dto.isActive
        )
    }
}
