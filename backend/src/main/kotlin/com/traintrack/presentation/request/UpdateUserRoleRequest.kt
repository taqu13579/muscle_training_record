package com.traintrack.presentation.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UpdateUserRoleRequest(
    @field:NotBlank(message = "ロールは必須です")
    @field:Pattern(
        regexp = "^(USER|ADMIN)$",
        message = "ロールは USER または ADMIN を指定してください"
    )
    val role: String
)
