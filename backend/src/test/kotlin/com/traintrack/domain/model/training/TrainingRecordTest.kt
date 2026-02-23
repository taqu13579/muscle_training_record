package com.traintrack.domain.model.training

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

class TrainingRecordTest {

    @Test
    fun `トレーニング記録を作成できる`() {
        val record = TrainingRecord.createNew(
            exerciseId = 1,
            weightKg = BigDecimal("80.00"),
            repCount = 10,
            setCount = 3,
            trainingDate = LocalDate.of(2026, 2, 22),
            memo = "フォームを意識"
        )

        assertEquals(1L, record.exerciseId.value)
        assertEquals(BigDecimal("80.00"), record.weight.value)
        assertEquals(10, record.repCount.value)
        assertEquals(3, record.setCount.value)
        assertEquals(LocalDate.of(2026, 2, 22), record.trainingDate.value)
        assertEquals("フォームを意識", record.memo)
    }

    @Test
    fun `総ボリュームが正しく計算される`() {
        val record = TrainingRecord.createNew(
            exerciseId = 1,
            weightKg = BigDecimal("100.00"),
            repCount = 10,
            setCount = 3,
            trainingDate = LocalDate.of(2026, 2, 22)
        )

        // 100kg × 10回 × 3セット = 3000
        assertEquals(BigDecimal("3000.00"), record.totalVolume)
    }

    @Test
    fun `重量を更新できる`() {
        val record = TrainingRecord.createNew(
            exerciseId = 1,
            weightKg = BigDecimal("80.00"),
            repCount = 10,
            setCount = 3,
            trainingDate = LocalDate.of(2026, 2, 22)
        )

        val updated = record.updateWeight(BigDecimal("85.00"))
        assertEquals(BigDecimal("85.00"), updated.weight.value)
    }

    @Test
    fun `回数を更新できる`() {
        val record = TrainingRecord.createNew(
            exerciseId = 1,
            weightKg = BigDecimal("80.00"),
            repCount = 10,
            setCount = 3,
            trainingDate = LocalDate.of(2026, 2, 22)
        )

        val updated = record.updateRepCount(12)
        assertEquals(12, updated.repCount.value)
    }
}
