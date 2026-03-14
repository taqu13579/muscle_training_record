package com.traintrack.presentation.response

import com.traintrack.application.dto.BodyWeightDto
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class BodyWeightResponse(
    val id: Long,
    val weightKg: BigDecimal,
    val recordedDate: LocalDate,
    val memo: String?,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(dto: BodyWeightDto) = BodyWeightResponse(
            id = dto.id,
            weightKg = dto.weightKg,
            recordedDate = dto.recordedDate,
            memo = dto.memo,
            createdAt = dto.createdAt
        )
    }
}
