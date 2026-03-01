package com.traintrack.domain.model.training

@JvmInline
value class TrainingRecordId(val value: Long) {
    init {
        require(value >= 0) { "TrainingRecordId must be non-negative" }
    }

    fun isNew(): Boolean = value == 0L
}
