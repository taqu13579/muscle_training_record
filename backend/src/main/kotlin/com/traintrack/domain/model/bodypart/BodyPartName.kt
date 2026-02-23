package com.traintrack.domain.model.bodypart

@JvmInline
value class BodyPartName(val value: String) {
    init {
        require(value.isNotBlank()) { "BodyPartName must not be blank" }
        require(value.length <= 50) { "BodyPartName must be 50 characters or less" }
    }
}
