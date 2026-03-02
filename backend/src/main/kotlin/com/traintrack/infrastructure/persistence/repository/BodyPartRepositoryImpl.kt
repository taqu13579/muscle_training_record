package com.traintrack.infrastructure.persistence.repository

import com.traintrack.domain.model.bodypart.BodyPart
import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.repository.BodyPartRepository
import com.traintrack.infrastructure.persistence.entity.BodyPartEntity
import com.traintrack.infrastructure.persistence.mapper.BodyPartMapper
import com.traintrack.infrastructure.persistence.repository.jpa.JpaBodyPartRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

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

    override fun existsByName(name: String): Boolean {
        return jpaRepository.existsByName(name)
    }

    @Transactional
    override fun create(name: String, displayOrder: Int): BodyPart {
        val entity = BodyPartEntity(name = name, displayOrder = displayOrder)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    @Transactional
    override fun update(bodyPart: BodyPart): BodyPart {
        val existing = jpaRepository.findById(bodyPart.id.value)
            .orElseThrow { IllegalArgumentException("部位が見つかりません: ${bodyPart.id.value}") }
        val updated = BodyPartEntity(
            id = existing.id,
            name = bodyPart.name.value,
            displayOrder = bodyPart.displayOrder,
            createdAt = existing.createdAt
        )
        val saved = jpaRepository.save(updated)
        return mapper.toDomain(saved)
    }

    @Transactional
    override fun deleteById(id: BodyPartId) {
        if (!jpaRepository.existsById(id.value)) {
            throw IllegalArgumentException("部位が見つかりません: ${id.value}")
        }
        jpaRepository.deleteById(id.value)
    }
}
