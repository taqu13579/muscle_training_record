package com.traintrack.domain.exception

sealed class AuthenticationException(message: String) : RuntimeException(message) {
    class InvalidCredentials : AuthenticationException("メールアドレスまたはパスワードが正しくありません")
}
