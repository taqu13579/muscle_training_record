package com.traintrack.infrastructure.persistence.mapper

import com.traintrack.domain.model.bodypart.BodyPart
import com.traintrack.infrastructure.persistence.entity.BodyPartEntity
import org.springframework.stereotype.Component

@Component
class BodyPartMapper {
    fun toDomain(entity: BodyPartEntity): BodyPart = BodyPart.create(
        id = entity.id,
        name = entity.name,
        displayOrder = entity.displayOrder
    )

    fun toDomainList(entities: List<BodyPartEntity>): List<BodyPart> = entities.map { toDomain(it) }
}
