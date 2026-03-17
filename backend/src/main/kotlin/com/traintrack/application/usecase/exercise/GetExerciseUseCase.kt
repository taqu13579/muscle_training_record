package com.traintrack.application.usecase.exercise

import com.traintrack.application.dto.ExerciseDto
import com.traintrack.domain.model.exercise.ExerciseId
import com.traintrack.domain.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetExerciseUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    @Transactional(readOnly = true)
    fun execute(id: Long): ExerciseDto {
        val exercise = exerciseRepository.findById(ExerciseId(id))
            ?: throw IllegalArgumentException("Exercise not found: $id")
        return ExerciseDto.from(exercise)
    }
}
