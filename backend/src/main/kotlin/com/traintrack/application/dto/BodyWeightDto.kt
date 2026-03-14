package com.traintrack.application.dto

import com.traintrack.domain.model.bodyweight.BodyWeight
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class BodyWeightDto(
    val id: Long,
    val weightKg: BigDecimal,
    val recordedDate: LocalDate,
    val memo: String?,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(domain: BodyWeight) = BodyWeightDto(
            id = domain.id.value,
            weightKg = domain.weightKg,
            recordedDate = domain.recordedDate,
            memo = domain.memo,
            createdAt = domain.createdAt
        )
    }
}
