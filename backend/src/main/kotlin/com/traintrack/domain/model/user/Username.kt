package com.traintrack.domain.model.user

@JvmInline
value class Username(val value: String) {
    init {
        require(value.isNotBlank()) { "ユーザー名は必須です" }
        require(value.length in 3..50) { "ユーザー名は3〜50文字で入力してください" }
        require(value.matches(VALID_PATTERN)) {
            "ユーザー名には英数字、アンダースコア、ハイフンのみ使用できます"
        }
    }

    companion object {
        private val VALID_PATTERN = Regex("^[a-zA-Z0-9_-]+$")
    }
}
