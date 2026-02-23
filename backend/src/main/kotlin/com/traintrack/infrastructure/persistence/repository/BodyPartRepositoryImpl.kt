package com.traintrack.infrastructure.persistence.repository

import com.traintrack.domain.model.bodypart.BodyPart
import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.repository.BodyPartRepository
import com.traintrack.infrastructure.persistence.mapper.BodyPartMapper
import com.traintrack.infrastructure.persistence.repository.jpa.JpaBodyPartRepository
import org.springframework.stereotype.Repository

@Repository
class BodyPartRepositoryImpl(
    private val jpaRepository: JpaBodyPartRepository,
    private val mapper: BodyPartMapper
) : BodyPartRepository {

    override fun findAll(): List<BodyPart> {
        return mapper.toDomainList(jpaRepository.findAllByOrderByDisplayOrderAsc())
    }

    override fun findById(id: BodyPartId): BodyPart? {
        return jpaRepository.findById(id.value)
            .map { mapper.toDomain(it) }
            .orElse(null)
    }
}
