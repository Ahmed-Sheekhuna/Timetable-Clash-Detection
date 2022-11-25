package com.timetable.service

import com.timetable.entity.ProgramEntity
import com.timetable.entity.TermEntity
import com.timetable.models.Program
import com.timetable.models.Term
import com.timetable.repository.ModuleRepository
import com.timetable.repository.ProgramRepository
import com.timetable.repository.TermRepository
import com.timetable.service.mapper.ProgramMapper
import com.timetable.service.mapper.TermMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class ProgramService(@Autowired private val termRepository: TermRepository,
                     @Autowired private val programRepository: ProgramRepository,
                     @Autowired private val programMapper: ProgramMapper) {
    fun saveProgram(program: Program): Long {
        val entity: ProgramEntity = programMapper.toEntity(program)
        entity.terms = termRepository.findAllById(program.terms.map { it.id }.toList()).toList()
        return programRepository.save(entity).id!!
    }

    fun getProgram(id: Long): Program {
        val entity: ProgramEntity = programRepository.findById(id).orElseThrow()
        return programMapper.fromEntity(entity)
    }

    fun getAllPrograms(): List<Program> {
        return programRepository.findAll().toList().stream().map { programMapper.fromEntity(it) }.toList()
    }
}