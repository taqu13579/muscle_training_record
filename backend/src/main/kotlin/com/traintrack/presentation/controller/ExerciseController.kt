package com.traintrack.presentation.controller

import com.traintrack.application.usecase.exercise.*
import com.traintrack.presentation.request.CreateExerciseRequest
import com.traintrack.presentation.request.UpdateExerciseRequest
import com.traintrack.presentation.response.ExerciseResponse
import com.traintrack.presentation.security.CurrentUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/exercises")
class ExerciseController(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val getExerciseUseCase: GetExerciseUseCase,
    private val registerExerciseUseCase: RegisterExerciseUseCase,
    private val updateExerciseUseCase: UpdateExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase,
    private val currentUser: CurrentUser
) {
    @GetMapping
    fun getAll(
        @RequestParam(required = false) bodyPartId: Long?
    ): ResponseEntity<List<ExerciseResponse>> {
        val exercises = if (bodyPartId != null) {
            getExercisesUseCase.executeByBodyPart(bodyPartId)
        } else {
            getExercisesUseCase.execute()
        }
        return ResponseEntity.ok(exercises.map { ExerciseResponse.from(it) })
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ExerciseResponse> {
        val exercise = getExerciseUseCase.execute(id)
        return ResponseEntity.ok(ExerciseResponse.from(exercise))
    }

    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateExerciseRequest
    ): ResponseEntity<ExerciseResponse> {
        currentUser.requireAdmin()
        val command = RegisterExerciseCommand(
            name = request.name,
            bodyPartId = request.bodyPartId
        )
        val created = registerExerciseUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(ExerciseResponse.from(created))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateExerciseRequest
    ): ResponseEntity<ExerciseResponse> {
        currentUser.requireAdmin()
        val command = UpdateExerciseCommand(
            id = id,
            name = request.name,
            description = request.description,
            auxiliaryMuscleBodyPartIds = request.auxiliaryMuscleBodyPartIds
        )
        val updated = updateExerciseUseCase.execute(command)
        return ResponseEntity.ok(ExerciseResponse.from(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        currentUser.requireAdmin()
        deleteExerciseUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }
}
