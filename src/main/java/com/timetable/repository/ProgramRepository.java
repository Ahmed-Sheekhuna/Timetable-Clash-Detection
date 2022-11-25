package com.timetable.repository;

import com.timetable.entity.ProgramEntity;
import org.springframework.data.repository.CrudRepository;

//repository defines a lot of OOTB methods for retrieving data, i.e. findById, save, findAll, etc.
public interface ProgramRepository extends CrudRepository<ProgramEntity, Long> {
}
