package com.traintrack.domain.model.training

import java.math.BigDecimal

@JvmInline
value class Weight(val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO) { "Weight must be non-negative" }
        require(value <= BigDecimal("999.99")) { "Weight must be 999.99 or less" }
    }

    companion object {
        fun of(value: Double): Weight = Weight(BigDecimal.valueOf(value))
        fun of(value: String): Weight = Weight(BigDecimal(value))
    }

    fun toKg(): Double = value.toDouble()
}
