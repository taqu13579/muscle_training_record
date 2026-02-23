package com.traintrack.application.usecase.exercise

import com.traintrack.application.dto.ExerciseDto
import com.traintrack.domain.model.exercise.Exercise
import com.traintrack.domain.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class RegisterExerciseCommand(
    val name: String,
    val bodyPartId: Long
)

@Service
class RegisterExerciseUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    @Transactional
    fun execute(command: RegisterExerciseCommand): ExerciseDto {
        val exercise = Exercise.createNew(
            name = command.name,
            bodyPartId = command.bodyPartId
        )
        val saved = exerciseRepository.save(exercise)
        return ExerciseDto.from(saved)
    }
}
