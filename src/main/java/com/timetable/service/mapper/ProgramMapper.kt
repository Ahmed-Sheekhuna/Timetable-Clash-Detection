package com.timetable.service.mapper

import com.timetable.entity.ProgramEntity
import com.timetable.entity.TermEntity
import com.timetable.models.Program
import com.timetable.models.Term
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProgramMapper(@Autowired private val termMapper: TermMapper) {

    //mapper to convert between Entity classes and UI models
    fun toEntity(program: Program): ProgramEntity {
        return ProgramEntity(program.name, program.startYear, program.type,
                program.terms.stream().map { termMapper.toEntity(it) }.toList(),
                program.id)
    }

    fun fromEntity(entity: ProgramEntity): Program {
        return Program(entity.id, entity.name, entity.startYear, entity.type,
                entity.terms!!.stream().map { termMapper.fromEntity(it) }.toList())
    }
}