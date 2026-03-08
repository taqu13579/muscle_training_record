package com.traintrack.presentation.controller

import com.traintrack.application.usecase.user.CreateAdminUserCommand
import com.traintrack.application.usecase.user.CreateAdminUserUseCase
import com.traintrack.application.usecase.user.GetUsersUseCase
import com.traintrack.application.usecase.user.UpdateUserRoleCommand
import com.traintrack.application.usecase.user.UpdateUserRoleUseCase
import com.traintrack.presentation.request.CreateAdminUserRequest
import com.traintrack.presentation.request.UpdateUserRoleRequest
import com.traintrack.presentation.response.UserResponse
import com.traintrack.presentation.security.CurrentUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/users")
class AdminUserController(
    private val getUsersUseCase: GetUsersUseCase,
    private val updateUserRoleUseCase: UpdateUserRoleUseCase,
    private val createAdminUserUseCase: CreateAdminUserUseCase,
    private val currentUser: CurrentUser
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<UserResponse>> {
        currentUser.requireAdmin()
        val users = getUsersUseCase.execute()
        return ResponseEntity.ok(users.map { UserResponse.from(it) })
    }

    @PostMapping
    fun createAdminUser(
        @Valid @RequestBody request: CreateAdminUserRequest
    ): ResponseEntity<UserResponse> {
        currentUser.requireAdmin()
        val command = CreateAdminUserCommand(
            email = request.email,
            username = request.username,
            password = request.password
        )
        val created = createAdminUserUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created))
    }

    @PatchMapping("/{id}/role")
    fun updateRole(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRoleRequest
    ): ResponseEntity<UserResponse> {
        currentUser.requireAdmin()
        val command = UpdateUserRoleCommand(userId = id, role = request.role)
        val updated = updateUserRoleUseCase.execute(command)
        return ResponseEntity.ok(UserResponse.from(updated))
    }
}
