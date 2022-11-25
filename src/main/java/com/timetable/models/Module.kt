package com.timetable.models

data class Module(
        var id: Long? = null,
        var name: String? = null,
        var required: Boolean? = null,
        var activities: List<Activity>? = null)