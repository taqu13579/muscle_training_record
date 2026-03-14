package com.traintrack.infrastructure.persistence.repository

import com.traintrack.domain.model.bodyweight.BodyWeight
import com.traintrack.domain.model.bodyweight.BodyWeightId
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.BodyWeightRepository
import com.traintrack.infrastructure.persistence.entity.BodyWeightEntity
import com.traintrack.infrastructure.persistence.repository.jpa.JpaBodyWeightRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
class BodyWeightRepositoryImpl(
    private val jpaRepository: JpaBodyWeightRepository
) : BodyWeightRepository {

    override fun findAllByUserId(userId: UserId): List<BodyWeight> {
        return jpaRepository.findAllByUserIdOrderByRecordedDateDesc(userId.value).map { it.toDomain() }
    }

    override fun findByUserIdAndDateBetween(userId: UserId, startDate: LocalDate, endDate: LocalDate): List<BodyWeight> {
        return jpaRepository.findByUserIdAndRecordedDateBetweenOrderByRecordedDateAsc(
            userId.value, startDate, endDate
        ).map { it.toDomain() }
    }

    override fun findByIdAndUserId(id: BodyWeightId, userId: UserId): BodyWeight? {
        return jpaRepository.findByIdAndUserId(id.value, userId.value)?.toDomain()
    }

    @Transactional
    override fun save(bodyWeight: BodyWeight): BodyWeight {
        val entity = if (bodyWeight.id.value == 0L) {
            BodyWeightEntity(
                userId = bodyWeight.userId.value,
                weightKg = bodyWeight.weightKg,
                recordedDate = bodyWeight.recordedDate,
                memo = bodyWeight.memo
            )
        } else {
            val existing = jpaRepository.findByIdAndUserId(bodyWeight.id.value, bodyWeight.userId.value)
                ?: throw IllegalArgumentException("BodyWeight not found: ${bodyWeight.id.value}")
            existing.weightKg = bodyWeight.weightKg
            existing.recordedDate = bodyWeight.recordedDate
            existing.memo = bodyWeight.memo
            existing
        }
        return jpaRepository.save(entity).toDomain()
    }

    @Transactional
    override fun deleteByIdAndUserId(id: BodyWeightId, userId: UserId) {
        jpaRepository.deleteByIdAndUserId(id.value, userId.value)
    }

    private fun BodyWeightEntity.toDomain() = BodyWeight(
        id = BodyWeightId(id),
        userId = UserId(userId),
        weightKg = weightKg,
        recordedDate = recordedDate,
        memo = memo,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
