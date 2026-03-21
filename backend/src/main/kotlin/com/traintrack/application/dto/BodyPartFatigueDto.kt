package com.traintrack.application.dto

import java.math.BigDecimal

data class BodyPartFatigueDto(
    val bodyPartId: Long,
    val bodyPartName: String,
    val currentVolume: BigDecimal,
    val maxVolume: BigDecimal,
    val fatiguePercentage: Int
)
