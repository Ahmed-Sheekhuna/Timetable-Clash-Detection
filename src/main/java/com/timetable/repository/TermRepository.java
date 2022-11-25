package com.timetable.repository;

import com.timetable.entity.TermEntity;
import org.springframework.data.repository.CrudRepository;

//repository defines a lot of OOTB methods for retrieving data, i.e. findById, save, findAll, etc.
public interface TermRepository extends CrudRepository<TermEntity, Long> {
}
