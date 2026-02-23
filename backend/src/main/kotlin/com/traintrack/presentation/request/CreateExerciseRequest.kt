package com.traintrack.presentation.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class CreateExerciseRequest(
    @field:NotBlank(message = "種目名は必須です")
    @field:Size(max = 100, message = "種目名は100文字以内で入力してください")
    val name: String,

    @field:Positive(message = "部位IDは正の数である必要があります")
    val bodyPartId: Long
)
