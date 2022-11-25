package com.timetable.models

data class Program(
        var id: Long? = null,
        var name: String? = null,
        var startYear: Int? = null,
        var type: String? = null,
        var terms: List<Term> = emptyList()) {
    override fun toString(): String = "ID [$id] - NAME [$name] - YEAR [$startYear] - TYPE [$type] - TERMS AMOUNT [${terms.size}]"
}