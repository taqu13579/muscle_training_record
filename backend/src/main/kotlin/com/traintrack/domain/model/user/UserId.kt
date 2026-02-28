package com.traintrack.domain.model.user

@JvmInline
value class UserId(val value: Long) {
    init {
        require(value >= 0) { "UserId must be non-negative" }
    }

    fun isNew(): Boolean = value == 0L
}
