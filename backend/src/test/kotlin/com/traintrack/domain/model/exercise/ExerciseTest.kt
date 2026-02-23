package com.traintrack.domain.model.exercise

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExerciseTest {

    @Test
    fun `種目を作成できる`() {
        val exercise = Exercise.createNew(
            name = "ベンチプレス",
            bodyPartId = 1
        )

        assertEquals("ベンチプレス", exercise.name.value)
        assertEquals(1L, exercise.bodyPartId.value)
        assertTrue(exercise.isActive)
    }

    @Test
    fun `種目を非アクティブにできる`() {
        val exercise = Exercise.createNew(
            name = "ベンチプレス",
            bodyPartId = 1
        )

        val deactivated = exercise.deactivate()
        assertFalse(deactivated.isActive)
    }

    @Test
    fun `種目を再アクティブにできる`() {
        val exercise = Exercise.createNew(
            name = "ベンチプレス",
            bodyPartId = 1
        ).deactivate()

        val activated = exercise.activate()
        assertTrue(activated.isActive)
    }

    @Test
    fun `種目名を更新できる`() {
        val exercise = Exercise.createNew(
            name = "ベンチプレス",
            bodyPartId = 1
        )

        val updated = exercise.updateName("インクラインベンチプレス")
        assertEquals("インクラインベンチプレス", updated.name.value)
    }
}
