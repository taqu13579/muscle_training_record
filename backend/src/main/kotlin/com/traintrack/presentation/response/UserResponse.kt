package com.traintrack.presentation.response

import com.traintrack.application.dto.UserDto
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(dto: UserDto): UserResponse = UserResponse(
            id = dto.id,
            email = dto.email,
            username = dto.username,
            createdAt = dto.createdAt
        )
    }
}
