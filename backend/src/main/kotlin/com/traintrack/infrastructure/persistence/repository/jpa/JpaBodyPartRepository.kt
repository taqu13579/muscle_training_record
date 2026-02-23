package com.traintrack.infrastructure.persistence.repository.jpa

import com.traintrack.infrastructure.persistence.entity.BodyPartEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaBodyPartRepository : JpaRepository<BodyPartEntity, Long> {
    fun findAllByOrderByDisplayOrderAsc(): List<BodyPartEntity>
}
