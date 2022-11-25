package com.timetable.service

import com.timetable.entity.ActivityEntity
import com.timetable.models.Activity
import com.timetable.repository.ActivityRepository
import com.timetable.service.mapper.ActivityMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ActivityService(@Autowired private val activityRepository: ActivityRepository,
                      @Autowired private val activityMapper: ActivityMapper) {
    fun saveActivity(activity: Activity): Long {
        return activityRepository.save(activityMapper.toEntity(activity)).id!!
    }

    fun getActivity(id: Long): Activity {
        val entity: ActivityEntity = activityRepository.findById(id).orElseThrow()
        return activityMapper.fromEntity(entity)
    }
}