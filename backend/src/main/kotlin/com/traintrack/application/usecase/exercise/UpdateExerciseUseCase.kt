package com.traintrack.application.usecase.exercise

import com.traintrack.application.dto.ExerciseDto
import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.model.exercise.ExerciseId
import com.traintrack.domain.repository.BodyPartRepository
import com.traintrack.domain.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class UpdateExerciseCommand(
    val id: Long,
    val name: String,
    val description: String? = null,
    val auxiliaryMuscleBodyPartIds: List<Long> = emptyList()
)

@Service
class UpdateExerciseUseCase(
    private val exerciseRepository: ExerciseRepository,
    private val bodyPartRepository: BodyPartRepository
) {
    @Transactional
    fun execute(command: UpdateExerciseCommand): ExerciseDto {
        val existing = exerciseRepository.findById(ExerciseId(command.id))
            ?: throw IllegalArgumentException("Exercise not found: ${command.id}")

        val auxiliaryMuscles = command.auxiliaryMuscleBodyPartIds.mapNotNull { id ->
            bodyPartRepository.findById(BodyPartId(id))
        }

        val updated = existing.updateDetail(
            newName = command.name,
            newDescription = command.description,
            newAuxiliaryMuscles = auxiliaryMuscles
        )
        val saved = exerciseRepository.save(updated)
        return ExerciseDto.from(saved)
    }
}
