package com.traintrack.domain.model.training

@JvmInline
value class RepCount(val value: Int) {
    init {
        require(value > 0) { "RepCount must be positive" }
        require(value <= 1000) { "RepCount must be 1000 or less" }
    }
}
