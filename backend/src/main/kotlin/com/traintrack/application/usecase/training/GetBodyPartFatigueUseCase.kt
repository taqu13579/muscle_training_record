package com.traintrack.application.usecase.training

import com.traintrack.application.dto.BodyPartFatigueDto
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.BodyPartRepository
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class GetBodyPartFatigueUseCase(
    private val trainingRecordRepo: TrainingRecordRepository,
    private val bodyPartRepo: BodyPartRepository
) {
    @Transactional(readOnly = true)
    fun execute(userId: Long): List<BodyPartFatigueDto> {
        val today = LocalDate.now()
        val startDate = today.minusDays(89)

        val records = trainingRecordRepo.findByUserIdAndTrainingDateBetween(UserId(userId), startDate, today)

        // bodyPartId -> (date -> volume)
        val volumeMap = mutableMapOf<Long, MutableMap<LocalDate, BigDecimal>>()

        for (record in records) {
            val exercise = record.exercise ?: continue
            val volume = record.totalVolume
            val date = record.trainingDate.value

            exercise.bodyPart?.let { bp ->
                volumeMap.getOrPut(bp.id.value) { mutableMapOf() }
                    .merge(date, volume, BigDecimal::add)
            }

            for (aux in exercise.auxiliaryMuscles) {
                volumeMap.getOrPut(aux.id.value) { mutableMapOf() }
                    .merge(date, volume, BigDecimal::add)
            }
        }

        val allBodyParts = bodyPartRepo.findAll()

        return allBodyParts.sortedBy { it.displayOrder }.map { bodyPart ->
            val dateVolumes = volumeMap[bodyPart.id.value] ?: emptyMap()

            val currentVolume = (0L..2L).fold(BigDecimal.ZERO) { acc, offset ->
                acc + (dateVolumes[today.minusDays(offset)] ?: BigDecimal.ZERO)
            }

            val maxVolume = (0L..87L).maxOfOrNull { offset ->
                val windowEnd = today.minusDays(offset)
                (0L..2L).fold(BigDecimal.ZERO) { acc, d ->
                    acc + (dateVolumes[windowEnd.minusDays(d)] ?: BigDecimal.ZERO)
                }
            } ?: BigDecimal.ZERO

            val fatiguePercentage = if (maxVolume > BigDecimal.ZERO) {
                currentVolume.divide(maxVolume, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal(100))
                    .toInt()
                    .coerceAtMost(100)
            } else 0

            BodyPartFatigueDto(
                bodyPartId = bodyPart.id.value,
                bodyPartName = bodyPart.name.value,
                currentVolume = currentVolume,
                maxVolume = maxVolume,
                fatiguePercentage = fatiguePercentage
            )
        }
    }
}
