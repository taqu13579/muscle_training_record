package com.traintrack.application.dto

import com.traintrack.domain.model.bodypart.BodyPart

data class BodyPartDto(
    val id: Long,
    val name: String,
    val displayOrder: Int
) {
    companion object {
        fun from(domain: BodyPart): BodyPartDto = BodyPartDto(
            id = domain.id.value,
            name = domain.name.value,
            displayOrder = domain.displayOrder
        )
    }
}
