package com.traintrack.application.usecase.exercise

import com.traintrack.application.dto.ExerciseDto
import com.traintrack.domain.model.exercise.ExerciseId
import com.traintrack.domain.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class UpdateExerciseCommand(
    val id: Long,
    val name: String
)

@Service
class UpdateExerciseUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    @Transactional
    fun execute(command: UpdateExerciseCommand): ExerciseDto {
        val existing = exerciseRepository.findById(ExerciseId(command.id))
            ?: throw IllegalArgumentException("Exercise not found: ${command.id}")

        val updated = existing.updateName(command.name)
        val saved = exerciseRepository.save(updated)
        return ExerciseDto.from(saved)
    }
}
