package com.traintrack.presentation.response

import com.traintrack.application.dto.DailyVolumeDto
import java.math.BigDecimal

data class DailyVolumeResponse(
    val date: String,
    val totalVolume: BigDecimal
) {
    companion object {
        fun from(dto: DailyVolumeDto) = DailyVolumeResponse(
            date = dto.date.toString(),
            totalVolume = dto.totalVolume
        )
    }
}
