package com.timetable.repository;

import com.timetable.entity.ModuleEntity;
import org.springframework.data.repository.CrudRepository;

//repository defines a lot of OOTB methods for retrieving data, i.e. findById, save, findAll, etc.
public interface ModuleRepository extends CrudRepository<ModuleEntity, Long> {
}
