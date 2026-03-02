package com.traintrack.presentation.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class UpdateBodyPartRequest(
    @field:NotBlank(message = "部位名は必須です")
    @field:Size(max = 50, message = "部位名は50文字以内で入力してください")
    val name: String,

    @field:Positive(message = "表示順は1以上を指定してください")
    val displayOrder: Int
)
