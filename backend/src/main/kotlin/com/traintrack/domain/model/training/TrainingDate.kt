package com.traintrack.domain.model.training

import java.time.LocalDate

@JvmInline
value class TrainingDate(val value: LocalDate) {
    init {
        require(!value.isAfter(LocalDate.now().plusDays(1))) { "TrainingDate cannot be in the future" }
    }

    companion object {
        fun of(date: LocalDate): TrainingDate = TrainingDate(date)
        fun today(): TrainingDate = TrainingDate(LocalDate.now())
    }
}
