package com.traintrack.presentation.controller

import com.traintrack.application.usecase.user.RegisterUserCommand
import com.traintrack.application.usecase.user.RegisterUserUseCase
import com.traintrack.presentation.request.RegisterUserRequest
import com.traintrack.presentation.response.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val registerUserUseCase: RegisterUserUseCase
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterUserRequest
    ): ResponseEntity<UserResponse> {
        val command = RegisterUserCommand(
            email = request.email,
            username = request.username,
            password = request.password
        )
        val user = registerUserUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user))
    }
}
