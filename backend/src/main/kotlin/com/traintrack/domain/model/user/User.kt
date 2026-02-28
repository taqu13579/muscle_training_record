package com.traintrack.domain.model.user

import java.time.LocalDateTime

data class User(
    val id: UserId,
    val email: Email,
    val username: Username,
    val hashedPassword: HashedPassword,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun create(
            id: Long,
            email: String,
            username: String,
            hashedPassword: String,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null
        ): User = User(
            id = UserId(id),
            email = Email(email),
            username = Username(username),
            hashedPassword = HashedPassword(hashedPassword),
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        fun createNew(
            email: String,
            username: String,
            hashedPassword: String
        ): User = User(
            id = UserId(0),
            email = Email(email),
            username = Username(username),
            hashedPassword = HashedPassword(hashedPassword)
        )
    }
}
