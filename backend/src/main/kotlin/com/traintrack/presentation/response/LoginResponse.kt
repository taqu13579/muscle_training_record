package com.traintrack.presentation.response

import com.traintrack.application.usecase.user.LoginResult

data class LoginResponse(
    val user: UserResponse,
    val accessToken: String
) {
    companion object {
        fun from(result: LoginResult): LoginResponse = LoginResponse(
            user = UserResponse.from(result.user),
            accessToken = result.accessToken
        )
    }
}
