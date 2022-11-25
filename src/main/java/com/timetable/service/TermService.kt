package com.timetable.service

import com.timetable.entity.TermEntity
import com.timetable.models.Term
import com.timetable.repository.ModuleRepository
import com.timetable.repository.TermRepository
import com.timetable.service.mapper.TermMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TermService(@Autowired private val moduleRepository: ModuleRepository,
                  @Autowired private val termRepository: TermRepository,
                  @Autowired private val termMapper: TermMapper) {
    fun saveTerm(term: Term): Long {
        val entity: TermEntity = termMapper.toEntity(term)
        entity.modules = moduleRepository.findAllById(term.modules.map { it.id }.toList()).toList()
        return termRepository.save(entity).id!!
    }

    fun getTerm(id: Long): Term {
        val entity: TermEntity = termRepository.findById(id).orElseThrow()
        return termMapper.fromEntity(entity)
    }
}