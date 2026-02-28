package com.traintrack.domain.model.user

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "メールアドレスは必須です" }
        require(value.length <= 255) { "メールアドレスは255文字以内で入力してください" }
        require(isValidEmail(value)) { "有効なメールアドレスを入力してください" }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        private fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)
    }
}
