package com.timetable.service.mapper

import com.timetable.entity.ModuleEntity
import com.timetable.models.Module
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModuleMapper(@Autowired private val activityMapper: ActivityMapper) {

    //mapper to convert between Entity classes and UI models
    fun toEntity(module: Module): ModuleEntity {
        return ModuleEntity(
                module.name, module.required,
                module.activities?.stream()?.map { activityMapper.toEntity(it) }?.toList(),
                module.id)
    }

    fun fromEntity(entity: ModuleEntity): Module {
        return Module(entity.id,
                entity.name, entity.required, entity.activities!!.stream().map { activityMapper.fromEntity(it) }.toList())
    }
}