package com.traintrack.domain.model.exercise

@JvmInline
value class ExerciseName(val value: String) {
    init {
        require(value.isNotBlank()) { "ExerciseName must not be blank" }
        require(value.length <= 100) { "ExerciseName must be 100 characters or less" }
    }
}
