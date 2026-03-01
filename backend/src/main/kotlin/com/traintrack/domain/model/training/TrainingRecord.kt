package com.traintrack.domain.model.training

import com.traintrack.domain.model.exercise.Exercise
import com.traintrack.domain.model.exercise.ExerciseId
import com.traintrack.domain.model.user.UserId
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class TrainingRecord(
    val id: TrainingRecordId,
    val userId: UserId,
    val exerciseId: ExerciseId,
    val exercise: Exercise? = null,
    val weight: Weight,
    val repCount: RepCount,
    val setCount: SetCount,
    val trainingDate: TrainingDate,
    val memo: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    val totalVolume: BigDecimal
        get() = weight.value.multiply(BigDecimal(repCount.value * setCount.value))

    companion object {
        fun create(
            id: Long,
            userId: Long,
            exerciseId: Long,
            exercise: Exercise? = null,
            weightKg: BigDecimal,
            repCount: Int,
            setCount: Int,
            trainingDate: LocalDate,
            memo: String? = null,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null
        ): TrainingRecord = TrainingRecord(
            id = TrainingRecordId(id),
            userId = UserId(userId),
            exerciseId = ExerciseId(exerciseId),
            exercise = exercise,
            weight = Weight(weightKg),
            repCount = RepCount(repCount),
            setCount = SetCount(setCount),
            trainingDate = TrainingDate(trainingDate),
            memo = memo,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        fun createNew(
            userId: Long,
            exerciseId: Long,
            weightKg: BigDecimal,
            repCount: Int,
            setCount: Int,
            trainingDate: LocalDate,
            memo: String? = null
        ): TrainingRecord = TrainingRecord(
            id = TrainingRecordId(0),
            userId = UserId(userId),
            exerciseId = ExerciseId(exerciseId),
            weight = Weight(weightKg),
            repCount = RepCount(repCount),
            setCount = SetCount(setCount),
            trainingDate = TrainingDate(trainingDate),
            memo = memo
        )
    }

    fun updateWeight(newWeight: BigDecimal): TrainingRecord = copy(weight = Weight(newWeight))

    fun updateRepCount(newRepCount: Int): TrainingRecord = copy(repCount = RepCount(newRepCount))

    fun updateSetCount(newSetCount: Int): TrainingRecord = copy(setCount = SetCount(newSetCount))

    fun updateMemo(newMemo: String?): TrainingRecord = copy(memo = newMemo)
}
