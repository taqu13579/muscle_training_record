package com.traintrack.infrastructure.persistence.repository.jpa

import com.traintrack.infrastructure.persistence.entity.BodyWeightEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface JpaBodyWeightRepository : JpaRepository<BodyWeightEntity, Long> {
    fun findAllByUserIdOrderByRecordedDateDesc(userId: Long): List<BodyWeightEntity>
    fun findByUserIdAndRecordedDateBetweenOrderByRecordedDateAsc(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<BodyWeightEntity>
    fun findByIdAndUserId(id: Long, userId: Long): BodyWeightEntity?
    fun deleteByIdAndUserId(id: Long, userId: Long)
}
