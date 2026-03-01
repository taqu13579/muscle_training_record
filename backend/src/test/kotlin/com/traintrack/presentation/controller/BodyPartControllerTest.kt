package com.traintrack.presentation.controller

import com.traintrack.application.dto.BodyPartDto
import com.traintrack.application.usecase.bodypart.GetBodyPartsUseCase
import com.traintrack.infrastructure.security.JwtConfig
import com.traintrack.infrastructure.security.JwtProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(BodyPartController::class)
class BodyPartControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @TestConfiguration
    class Config {
        @Bean
        fun getBodyPartsUseCase(): GetBodyPartsUseCase = mockk()

        @Bean
        fun jwtConfig(): JwtConfig = JwtConfig(secret = "test-secret-key-minimum-32-characters", expirationMs = 1800000)

        @Bean
        fun jwtProvider(jwtConfig: JwtConfig): JwtProvider = JwtProvider(jwtConfig)
    }

    @Autowired
    private lateinit var getBodyPartsUseCase: GetBodyPartsUseCase

    @Test
    fun `部位一覧を取得できる`() {
        val bodyParts = listOf(
            BodyPartDto(id = 1, name = "胸", displayOrder = 1),
            BodyPartDto(id = 2, name = "背中", displayOrder = 2)
        )
        every { getBodyPartsUseCase.execute() } returns bodyParts

        mockMvc.get("/api/v1/body-parts") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].id") { value(1) }
            jsonPath("$[0].name") { value("胸") }
            jsonPath("$[1].id") { value(2) }
            jsonPath("$[1].name") { value("背中") }
        }
    }
}
