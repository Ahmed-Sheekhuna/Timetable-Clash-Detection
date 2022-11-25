package com.timetable.models

data class Term(
        var id: Long? = null,
        var termNumber: Int? = null,
        var startMonth: Int? = null,
        var endMonth: Int? = null,
        var year: Int? = null,
        var modules: List<Module> = emptyList()){
    override fun toString(): String = "${if(modules.size==4)"[CONFIGURED]" else "[NOT CONFIGURED]"} TERM NUMBER [$termNumber] - START MONTH [$startMonth] - END MONTH [$endMonth] - YEAR OF EDUCATION [$year]"
}