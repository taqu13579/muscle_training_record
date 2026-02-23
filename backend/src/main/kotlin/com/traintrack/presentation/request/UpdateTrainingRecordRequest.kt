package com.traintrack.presentation.request

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate

data class UpdateTrainingRecordRequest(
    @field:Positive(message = "種目IDは正の数である必要があります")
    val exerciseId: Long,

    @field:DecimalMin(value = "0.0", inclusive = true, message = "重量は0以上である必要があります")
    @field:DecimalMax(value = "999.99", message = "重量は999.99以下である必要があります")
    val weightKg: BigDecimal,

    @field:Min(value = 1, message = "回数は1以上である必要があります")
    @field:Max(value = 1000, message = "回数は1000以下である必要があります")
    val repCount: Int,

    @field:Min(value = 1, message = "セット数は1以上である必要があります")
    @field:Max(value = 100, message = "セット数は100以下である必要があります")
    val setCount: Int,

    @field:NotNull(message = "トレーニング日は必須です")
    val trainingDate: LocalDate,

    @field:Size(max = 1000, message = "メモは1000文字以内で入力してください")
    val memo: String? = null
)
