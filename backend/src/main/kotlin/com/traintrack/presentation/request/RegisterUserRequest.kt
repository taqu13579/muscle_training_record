package com.traintrack.presentation.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterUserRequest(
    @field:NotBlank(message = "メールアドレスは必須です")
    @field:Email(message = "有効なメールアドレスを入力してください")
    @field:Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    val email: String,

    @field:NotBlank(message = "ユーザー名は必須です")
    @field:Size(min = 3, max = 50, message = "ユーザー名は3〜50文字で入力してください")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9_-]+$",
        message = "ユーザー名には英数字、アンダースコア、ハイフンのみ使用できます"
    )
    val username: String,

    @field:NotBlank(message = "パスワードは必須です")
    @field:Size(min = 8, max = 100, message = "パスワードは8〜100文字で入力してください")
    val password: String
)
