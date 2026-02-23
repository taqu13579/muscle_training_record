package com.traintrack.domain.repository

import com.traintrack.domain.model.bodypart.BodyPart
import com.traintrack.domain.model.bodypart.BodyPartId

interface BodyPartRepository {
    fun findAll(): List<BodyPart>
    fun findById(id: BodyPartId): BodyPart?
}
