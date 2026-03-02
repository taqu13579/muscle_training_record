package com.traintrack.presentation.controller

import com.traintrack.application.usecase.user.GetUsersUseCase
import com.traintrack.application.usecase.user.UpdateUserRoleCommand
import com.traintrack.application.usecase.user.UpdateUserRoleUseCase
import com.traintrack.presentation.request.UpdateUserRoleRequest
import com.traintrack.presentation.response.UserResponse
import com.traintrack.presentation.security.CurrentUser
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/users")
class AdminUserController(
    private val getUsersUseCase: GetUsersUseCase,
    private val updateUserRoleUseCase: UpdateUserRoleUseCase,
    private val currentUser: CurrentUser
) {
    @GetMapping
    fun getAll(): ResponseEntity<List<UserResponse>> {
        currentUser.requireAdmin()
        val users = getUsersUseCase.execute()
        return ResponseEntity.ok(users.map { UserResponse.from(it) })
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
