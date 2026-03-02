package com.traintrack.presentation.controller

import com.traintrack.application.usecase.bodypart.*
import com.traintrack.presentation.request.RegisterBodyPartRequest
import com.traintrack.presentation.request.UpdateBodyPartRequest
import com.traintrack.presentation.response.BodyPartResponse
import com.traintrack.presentation.security.CurrentUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/body-parts")
class BodyPartController(
    private val getBodyPartsUseCase: GetBodyPartsUseCase,
    private val registerBodyPartUseCase: RegisterBodyPartUseCase,
    private val updateBodyPartUseCase: UpdateBodyPartUseCase,
    private val deleteBodyPartUseCase: DeleteBodyPartUseCase,
    private val currentUser: CurrentUser
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<BodyPartResponse>> {
        val bodyParts = getBodyPartsUseCase.execute()
        return ResponseEntity.ok(bodyParts.map { BodyPartResponse.from(it) })
    }

    @PostMapping
    fun create(
        @Valid @RequestBody request: RegisterBodyPartRequest
    ): ResponseEntity<BodyPartResponse> {
        currentUser.requireAdmin()
        val command = RegisterBodyPartCommand(
            name = request.name,
            displayOrder = request.displayOrder
        )
        val created = registerBodyPartUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(BodyPartResponse.from(created))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateBodyPartRequest
    ): ResponseEntity<BodyPartResponse> {
        currentUser.requireAdmin()
        val command = UpdateBodyPartCommand(
            id = id,
            name = request.name,
            displayOrder = request.displayOrder
        )
        val updated = updateBodyPartUseCase.execute(command)
        return ResponseEntity.ok(BodyPartResponse.from(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        currentUser.requireAdmin()
        deleteBodyPartUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }
}
