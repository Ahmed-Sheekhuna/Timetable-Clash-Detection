package com.timetable.repository;

import com.timetable.entity.ActivityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//repository defines a lot of OOTB methods for retrieving data, i.e. findById, save, findAll, etc.
public interface ActivityRepository extends CrudRepository<ActivityEntity, Long> {
}
