package com.traintrack.presentation.response

import com.traintrack.application.dto.BodyPartDto

data class BodyPartResponse(
    val id: Long,
    val name: String,
    val displayOrder: Int
) {
    companion object {
        fun from(dto: BodyPartDto): BodyPartResponse = BodyPartResponse(
            id = dto.id,
            name = dto.name,
            displayOrder = dto.displayOrder
        )
    }
}
