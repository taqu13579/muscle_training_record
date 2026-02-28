package com.traintrack.infrastructure.persistence.repository.jpa

import com.traintrack.infrastructure.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun findByUsername(username: String): UserEntity?
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
}
