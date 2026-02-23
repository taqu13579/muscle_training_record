package com.traintrack.presentation.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateExerciseRequest(
    @field:NotBlank(message = "種目名は必須です")
    @field:Size(max = 100, message = "種目名は100文字以内で入力してください")
    val name: String
)
