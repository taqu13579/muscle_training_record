package com.traintrack.presentation.response

import java.time.LocalDate

data class CalendarResponse(
    val yearMonth: String,
    val days: List<CalendarDayResponse>
)

data class CalendarDayResponse(
    val date: LocalDate,
    val recordCount: Int,
    val bodyParts: List<String>
)
