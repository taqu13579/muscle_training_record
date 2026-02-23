package com.traintrack.domain.model.bodypart

@JvmInline
value class BodyPartId(val value: Long) {
    init {
        require(value > 0) { "BodyPartId must be positive" }
    }
}
