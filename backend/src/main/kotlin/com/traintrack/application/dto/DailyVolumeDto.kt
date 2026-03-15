package com.traintrack.application.dto

import java.math.BigDecimal
import java.time.LocalDate

data class DailyVolumeDto(
    val date: LocalDate,
    val totalVolume: BigDecimal
)
