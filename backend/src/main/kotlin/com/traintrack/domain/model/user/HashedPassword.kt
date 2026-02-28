package com.traintrack.domain.model.user

@JvmInline
value class HashedPassword(val value: String) {
    init {
        require(value.isNotBlank()) { "パスワードハッシュは必須です" }
    }
}
