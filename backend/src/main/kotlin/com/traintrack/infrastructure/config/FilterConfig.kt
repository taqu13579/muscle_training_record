package com.traintrack.infrastructure.config

import com.traintrack.infrastructure.security.JwtAuthenticationFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun jwtFilterRegistration(jwtAuthenticationFilter: JwtAuthenticationFilter): FilterRegistrationBean<JwtAuthenticationFilter> {
        val registration = FilterRegistrationBean<JwtAuthenticationFilter>()
        registration.filter = jwtAuthenticationFilter
        registration.addUrlPatterns("/api/*")
        registration.order = 1
        return registration
    }
}
