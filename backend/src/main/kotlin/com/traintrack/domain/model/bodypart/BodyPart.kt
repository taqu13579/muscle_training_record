package com.traintrack.domain.model.bodypart

data class BodyPart(
    val id: BodyPartId,
    val name: BodyPartName,
    val displayOrder: Int
) {
    companion object {
        fun create(
            id: Long,
            name: String,
            displayOrder: Int
        ): BodyPart = BodyPart(
            id = BodyPartId(id),
            name = BodyPartName(name),
            displayOrder = displayOrder
        )
    }
}
