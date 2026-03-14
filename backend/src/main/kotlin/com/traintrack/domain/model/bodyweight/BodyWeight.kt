package com.traintrack.domain.model.bodyweight

import com.traintrack.domain.model.user.UserId
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class BodyWeightId(val value: Long)

data class BodyWeight(
    val id: BodyWeightId,
    val userId: UserId,
    val weightKg: BigDecimal,
    val recordedDate: LocalDate,
    val memo: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun createNew(
            userId: Long,
            weightKg: BigDecimal,
            recordedDate: LocalDate,
            memo: String? = null
        ): BodyWeight = BodyWeight(
            id = BodyWeightId(0),
            userId = UserId(userId),
            weightKg = weightKg,
            recordedDate = recordedDate,
            memo = memo
        )
    }
}
