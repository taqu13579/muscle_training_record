package com.traintrack.domain.model.training

@JvmInline
value class SetCount(val value: Int) {
    init {
        require(value > 0) { "SetCount must be positive" }
        require(value <= 100) { "SetCount must be 100 or less" }
    }
}
