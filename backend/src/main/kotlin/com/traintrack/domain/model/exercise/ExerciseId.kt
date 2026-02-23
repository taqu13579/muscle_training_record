package com.traintrack.domain.model.exercise

@JvmInline
value class ExerciseId(val value: Long) {
    init {
        require(value > 0) { "ExerciseId must be positive" }
    }
}
