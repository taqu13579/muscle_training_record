package com.traintrack.application.usecase.training

import com.traintrack.application.dto.DailyVolumeDto
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.TrainingRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class GetVolumeStatsUseCase(private val repo: TrainingRecordRepository) {
    @Transactional(readOnly = true)
    fun execute(userId: Long, days: Int, exerciseId: Long?, bodyPartId: Long?): List<DailyVolumeDto> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(days.toLong() - 1)
        val records = repo.findByUserIdAndTrainingDateBetween(UserId(userId), startDate, endDate)
        val filtered = records.filter { r ->
            when {
                exerciseId != null -> r.exerciseId.value == exerciseId
                bodyPartId != null -> r.exercise?.bodyPart?.id?.value == bodyPartId
                else -> true
            }
        }
        return filtered
            .groupBy { it.trainingDate.value }
            .map { (date, recs) ->
                DailyVolumeDto(date, recs.sumOf { it.totalVolume })
            }
            .sortedBy { it.date }
    }
}
