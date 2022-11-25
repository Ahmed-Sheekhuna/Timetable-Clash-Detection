package com.timetable.models

import java.time.DayOfWeek
import java.time.LocalTime

data class Activity(
        var id: Long? = null,
        var name: String? = null,
        var type: String? = "lection",
        var start: LocalTime? = null,
        var end: LocalTime? = null,
        var day: DayOfWeek? = null)
