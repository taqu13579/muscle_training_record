package com.traintrack.infrastructure.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtProvider(private val jwtConfig: JwtConfig) {

    fun generateToken(userId: Long, email: String): String {
        val now = Date()
        val expiry = Date(now.time + jwtConfig.expirationMs)
        val key = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())

        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val key = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserIdFromToken(token: String): Long {
        val key = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())
        val claims = Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).payload
        return claims.subject.toLong()
    }
}
