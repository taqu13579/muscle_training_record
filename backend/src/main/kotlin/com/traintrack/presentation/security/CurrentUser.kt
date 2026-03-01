package com.traintrack.presentation.security

import com.traintrack.domain.exception.AuthenticationException
import com.traintrack.infrastructure.security.JwtAuthenticationFilter
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
class CurrentUser {

    fun getId(): Long {
        val request = getCurrentRequest()
        val userId = request.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE) as? Long
            ?: throw AuthenticationException.InvalidCredentials()
        return userId
    }

    fun getIdOrNull(): Long? {
        val request = getCurrentRequest()
        return request.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE) as? Long
    }

    private fun getCurrentRequest(): HttpServletRequest {
        val attributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
            ?: throw IllegalStateException("No request context available")
        return attributes.request
    }
}
