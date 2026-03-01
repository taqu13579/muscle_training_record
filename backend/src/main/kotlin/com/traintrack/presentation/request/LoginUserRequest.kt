package com.traintrack.presentation.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginUserRequest(
    @field:NotBlank(message = "メールアドレスは必須です")
    @field:Email(message = "有効なメールアドレスを入力してください")
    val email: String,

    @field:NotBlank(message = "パスワードは必須です")
    val password: String
)
