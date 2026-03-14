package com.traintrack.presentation.request

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate

data class CreateBodyWeightRequest(
    @field:DecimalMin(value = "1.0", message = "体重は1kg以上である必要があります")
    @field:DecimalMax(value = "999.99", message = "体重は999.99kg以下である必要があります")
    val weightKg: BigDecimal,

    @field:NotNull(message = "記録日は必須です")
    val recordedDate: LocalDate,

    @field:Size(max = 500, message = "メモは500文字以内で入力してください")
    val memo: String? = null
)
