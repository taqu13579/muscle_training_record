package com.traintrack.infrastructure.persistence.mapper

import com.traintrack.domain.model.user.User
import com.traintrack.infrastructure.persistence.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDomain(entity: UserEntity): User = User.create(
        id = entity.id,
        email = entity.email,
        username = entity.username,
        hashedPassword = entity.passwordHash,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toDomainList(entities: List<UserEntity>): List<User> =
        entities.map { toDomain(it) }
}
