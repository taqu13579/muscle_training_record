package com.traintrack.presentation.response

import com.traintrack.application.dto.BodyPartFatigueDto
import java.math.BigDecimal

data class BodyPartFatigueResponse(
    val bodyPartId: Long,
    val bodyPartName: String,
    val currentVolume: BigDecimal,
    val maxVolume: BigDecimal,
    val fatiguePercentage: Int
) {
    companion object {
        fun from(dto: BodyPartFatigueDto) = BodyPartFatigueResponse(
            bodyPartId = dto.bodyPartId,
            bodyPartName = dto.bodyPartName,
            currentVolume = dto.currentVolume,
            maxVolume = dto.maxVolume,
            fatiguePercentage = dto.fatiguePercentage
        )
    }
}
