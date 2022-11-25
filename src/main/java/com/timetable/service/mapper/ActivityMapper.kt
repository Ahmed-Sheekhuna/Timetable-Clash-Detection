package com.timetable.service.mapper

import com.timetable.entity.ActivityEntity
import com.timetable.models.Activity
import org.springframework.stereotype.Service

@Service
class ActivityMapper {
    //mapper to convert between Entity classes and UI models
    fun toEntity(activity: Activity): ActivityEntity {
        return ActivityEntity(
                activity.name, activity.type, activity.start,
                activity.end, activity.day, activity.id)
    }

    fun fromEntity(entity: ActivityEntity): Activity {
        return Activity(entity.id,
                entity.name, entity.type, entity.start,
                entity.end, entity.day)
    }
}