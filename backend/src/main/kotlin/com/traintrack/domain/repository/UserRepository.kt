package com.traintrack.domain.repository

import com.traintrack.domain.model.user.Email
import com.traintrack.domain.model.user.User
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.model.user.Username

interface UserRepository {
    fun findById(id: UserId): User?
    fun findByEmail(email: Email): User?
    fun findByUsername(username: Username): User?
    fun existsByEmail(email: Email): Boolean
    fun existsByUsername(username: Username): Boolean
    fun save(user: User): User
}
