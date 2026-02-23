package com.traintrack.presentation.controller

import com.traintrack.application.usecase.bodypart.GetBodyPartsUseCase
import com.traintrack.presentation.response.BodyPartResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/body-parts")
class BodyPartController(
    private val getBodyPartsUseCase: GetBodyPartsUseCase
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<BodyPartResponse>> {
        val bodyParts = getBodyPartsUseCase.execute()
        return ResponseEntity.ok(bodyParts.map { BodyPartResponse.from(it) })
    }
}
