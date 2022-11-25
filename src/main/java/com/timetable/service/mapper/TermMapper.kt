package com.timetable.service.mapper

import com.timetable.entity.TermEntity
import com.timetable.models.Term
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TermMapper(@Autowired private val moduleMapper: ModuleMapper) {

    //mapper to convert between Entity classes and UI models
    fun toEntity(term: Term): TermEntity {
        return TermEntity(term.termNumber, term.startMonth, term.endMonth,
                term.modules.stream().map { moduleMapper.toEntity(it) }.toList(),
                term.year, term.id)
    }

    fun fromEntity(entity: TermEntity): Term {
        return Term(entity.id, entity.termNumber, entity.startMonth, entity.endMonth, entity.year,
                entity.modules!!.stream().map { moduleMapper.fromEntity(it) }.distinct().toList())
    }
}