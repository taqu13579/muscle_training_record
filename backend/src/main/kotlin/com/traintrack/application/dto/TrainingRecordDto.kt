package com.traintrack.application.dto

import com.traintrack.domain.model.training.TrainingRecord
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class TrainingRecordDto(
    val id: Long,
    val exerciseId: Long,
    val exercise: ExerciseDto?,
    val weightKg: BigDecimal,
    val repCount: Int,
    val setCount: Int,
    val trainingDate: LocalDate,
    val memo: String?,
    val totalVolume: BigDecimal,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(domain: TrainingRecord): TrainingRecordDto = TrainingRecordDto(
            id = domain.id.value,
            exerciseId = domain.exerciseId.value,
            exercise = domain.exercise?.let { ExerciseDto.from(it) },
            weightKg = domain.weight.value,
            repCount = domain.repCount.value,
            setCount = domain.setCount.value,
            trainingDate = domain.trainingDate.value,
            memo = domain.memo,
            totalVolume = domain.totalVolume,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
}
