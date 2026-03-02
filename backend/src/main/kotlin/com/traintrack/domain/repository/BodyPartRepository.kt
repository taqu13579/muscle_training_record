package com.traintrack.domain.repository

import com.traintrack.domain.model.bodypart.BodyPart
import com.traintrack.domain.model.bodypart.BodyPartId

interface BodyPartRepository {
    fun findAll(): List<BodyPart>
    fun findById(id: BodyPartId): BodyPart?
    fun existsByName(name: String): Boolean
    fun create(name: String, displayOrder: Int): BodyPart
    fun update(bodyPart: BodyPart): BodyPart
    fun deleteById(id: BodyPartId)
}
