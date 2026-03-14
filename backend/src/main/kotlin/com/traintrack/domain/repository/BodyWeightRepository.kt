package com.traintrack.domain.repository

import com.traintrack.domain.model.bodyweight.BodyWeight
import com.traintrack.domain.model.bodyweight.BodyWeightId
import com.traintrack.domain.model.user.UserId
import java.time.LocalDate

interface BodyWeightRepository {
    fun findAllByUserId(userId: UserId): List<BodyWeight>
    fun findByUserIdAndDateBetween(userId: UserId, startDate: LocalDate, endDate: LocalDate): List<BodyWeight>
    fun findByIdAndUserId(id: BodyWeightId, userId: UserId): BodyWeight?
    fun save(bodyWeight: BodyWeight): BodyWeight
    fun deleteByIdAndUserId(id: BodyWeightId, userId: UserId)
}
