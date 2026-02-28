package com.traintrack.infrastructure.persistence.repository

import com.traintrack.domain.model.user.Email
import com.traintrack.domain.model.user.User
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.model.user.Username
import com.traintrack.domain.repository.UserRepository
import com.traintrack.infrastructure.persistence.entity.UserEntity
import com.traintrack.infrastructure.persistence.mapper.UserMapper
import com.traintrack.infrastructure.persistence.repository.jpa.JpaUserRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserRepositoryImpl(
    private val jpaRepository: JpaUserRepository,
    private val mapper: UserMapper
) : UserRepository {

    override fun findById(id: UserId): User? {
        return jpaRepository.findById(id.value).orElse(null)?.let { mapper.toDomain(it) }
    }

    override fun findByEmail(email: Email): User? {
        return jpaRepository.findByEmail(email.value)?.let { mapper.toDomain(it) }
    }

    override fun findByUsername(username: Username): User? {
        return jpaRepository.findByUsername(username.value)?.let { mapper.toDomain(it) }
    }

    override fun existsByEmail(email: Email): Boolean {
        return jpaRepository.existsByEmail(email.value)
    }

    override fun existsByUsername(username: Username): Boolean {
        return jpaRepository.existsByUsername(username.value)
    }

    @Transactional
    override fun save(user: User): User {
        val entity = if (user.id.isNew()) {
            UserEntity(
                email = user.email.value,
                username = user.username.value,
                passwordHash = user.hashedPassword.value
            )
        } else {
            val existing = jpaRepository.findById(user.id.value)
                .orElseThrow { IllegalArgumentException("User not found: ${user.id.value}") }
            UserEntity(
                id = existing.id,
                email = user.email.value,
                username = user.username.value,
                passwordHash = user.hashedPassword.value,
                createdAt = existing.createdAt
            )
        }

        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }
}
