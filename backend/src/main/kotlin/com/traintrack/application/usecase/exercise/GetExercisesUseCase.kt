package com.traintrack.application.usecase.exercise

import com.traintrack.application.dto.ExerciseDto
import com.traintrack.domain.model.bodypart.BodyPartId
import com.traintrack.domain.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetExercisesUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    @Transactional(readOnly = true)
    fun execute(): List<ExerciseDto> {
        return exerciseRepository.findAll().map { ExerciseDto.from(it) }
    }

    @Transactional(readOnly = true)
    fun executeByBodyPart(bodyPartId: Long): List<ExerciseDto> {
        return exerciseRepository.findByBodyPartId(BodyPartId(bodyPartId)).map { ExerciseDto.from(it) }
    }
}
