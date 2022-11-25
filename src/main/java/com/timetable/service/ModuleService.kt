package com.timetable.service

import com.timetable.entity.ModuleEntity
import com.timetable.models.Module
import com.timetable.repository.ActivityRepository
import com.timetable.repository.ModuleRepository
import com.timetable.service.mapper.ModuleMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModuleService(@Autowired private val moduleRepository: ModuleRepository,
                    @Autowired private val activityRepository: ActivityRepository,
                    @Autowired private val moduleMapper: ModuleMapper) {
    fun saveModule(module: Module): Long {
        val entity: ModuleEntity = moduleMapper.toEntity(module)
        entity.activities = activityRepository.findAllById(entity.activities!!.map { it.id }.toList()).toList()
        return moduleRepository.save(entity).id!!
    }

    fun getModule(id: Long): Module {
        val entity: ModuleEntity = moduleRepository.findById(id).orElseThrow()
        return moduleMapper.fromEntity(entity)
    }
}