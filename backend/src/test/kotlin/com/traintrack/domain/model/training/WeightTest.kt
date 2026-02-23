package com.traintrack.domain.model.training

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals

class WeightTest {

    @Test
    fun `有効な重量を作成できる`() {
        val weight = Weight.of(80.5)
        assertEquals(BigDecimal.valueOf(80.5), weight.value)
    }

    @Test
    fun `0kgの重量を作成できる`() {
        val weight = Weight(BigDecimal.ZERO)
        assertEquals(BigDecimal.ZERO, weight.value)
    }

    @Test
    fun `負の重量は作成できない`() {
        assertThrows<IllegalArgumentException> {
            Weight(BigDecimal("-1"))
        }
    }

    @Test
    fun `999_99kgを超える重量は作成できない`() {
        assertThrows<IllegalArgumentException> {
            Weight(BigDecimal("1000.00"))
        }
    }

    @Test
    fun `toKgでDouble値を取得できる`() {
        val weight = Weight.of(60.0)
        assertEquals(60.0, weight.toKg())
    }
}
