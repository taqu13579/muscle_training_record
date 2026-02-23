package com.traintrack.application.usecase.exercise

import com.traintrack.domain.model.exercise.ExerciseId
import com.traintrack.domain.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteExerciseUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    @Transactional
    fun execute(id: Long) {
        exerciseRepository.deleteById(ExerciseId(id))
    }
}
