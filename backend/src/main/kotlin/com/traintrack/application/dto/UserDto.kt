package com.traintrack.application.dto

import com.traintrack.domain.model.user.User
import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val email: String,
    val username: String,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(domain: User): UserDto = UserDto(
            id = domain.id.value,
            email = domain.email.value,
            username = domain.username.value,
            createdAt = domain.createdAt
        )
    }
}
