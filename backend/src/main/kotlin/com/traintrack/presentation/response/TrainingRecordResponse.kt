package com.traintrack.presentation.response

import com.traintrack.application.dto.TrainingRecordDto
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class TrainingRecordResponse(
    val id: Long,
    val exercise: ExerciseResponse?,
    val weightKg: BigDecimal,
    val repCount: Int,
    val setCount: Int,
    val trainingDate: LocalDate,
    val memo: String?,
    val totalVolume: BigDecimal,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(dto: TrainingRecordDto): TrainingRecordResponse = TrainingRecordResponse(
            id = dto.id,
            exercise = dto.exercise?.let { ExerciseResponse.from(it) },
            weightKg = dto.weightKg,
            repCount = dto.repCount,
            setCount = dto.setCount,
            trainingDate = dto.trainingDate,
            memo = dto.memo,
            totalVolume = dto.totalVolume,
            createdAt = dto.createdAt
        )
    }
}
