package com.traintrack.presentation.controller

import com.traintrack.application.usecase.bodyweight.DeleteBodyWeightUseCase
import com.traintrack.application.usecase.bodyweight.GetBodyWeightsUseCase
import com.traintrack.application.usecase.bodyweight.RegisterBodyWeightCommand
import com.traintrack.application.usecase.bodyweight.RegisterBodyWeightUseCase
import com.traintrack.presentation.request.CreateBodyWeightRequest
import com.traintrack.presentation.response.BodyWeightResponse
import com.traintrack.presentation.security.CurrentUser
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/body-weight")
class BodyWeightController(
    private val getBodyWeightsUseCase: GetBodyWeightsUseCase,
    private val registerBodyWeightUseCase: RegisterBodyWeightUseCase,
    private val deleteBodyWeightUseCase: DeleteBodyWeightUseCase,
    private val currentUser: CurrentUser
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<BodyWeightResponse>> {
        val userId = currentUser.getId()
        val records = getBodyWeightsUseCase.execute(userId)
        return ResponseEntity.ok(records.map { BodyWeightResponse.from(it) })
    }

    @GetMapping("/range")
    fun getByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<List<BodyWeightResponse>> {
        val userId = currentUser.getId()
        val records = getBodyWeightsUseCase.executeByDateRange(userId, startDate, endDate)
        return ResponseEntity.ok(records.map { BodyWeightResponse.from(it) })
    }

    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateBodyWeightRequest
    ): ResponseEntity<BodyWeightResponse> {
        val userId = currentUser.getId()
        val command = RegisterBodyWeightCommand(
            userId = userId,
            weightKg = request.weightKg,
            recordedDate = request.recordedDate,
            memo = request.memo
        )
        val created = registerBodyWeightUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(BodyWeightResponse.from(created))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        val userId = currentUser.getId()
        deleteBodyWeightUseCase.execute(userId, id)
        return ResponseEntity.noContent().build()
    }
}
