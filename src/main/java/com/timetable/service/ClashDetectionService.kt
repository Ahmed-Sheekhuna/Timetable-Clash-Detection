package com.timetable.service

import com.timetable.models.Activity
import com.timetable.models.Module
import com.timetable.models.Term
import org.springframework.stereotype.Service

@Service
class ClashDetectionService {
    fun checkCollisions(term: Term): List<String> {
        //as terms are provided on different months
        val errors: MutableList<String> = ArrayList()
        val modules = term.modules
        //list of modules that are already checked to avoid duplications
        val checkedModules: MutableList<Module> = ArrayList()
        for (currentModule in modules) {
            println("start module processing ${currentModule.id}")
            //list of activities that are already checked to avoid duplications
            val checkedActivities: MutableList<Activity> = ArrayList()
            for (activity in currentModule.activities!!) {
                for (moduleToCheck in modules) {
                    if (checkedModules.contains(moduleToCheck)) {
                        continue
                    }
                    val day = activity.day
                    //find activities on the same day
                    val activityOfTheSameDay = moduleToCheck.activities!!.stream().filter {
                        it.day == day &&
                                it.id != activity.id &&
                                !checkedActivities.contains(it)
                    }.toList()
                    for (compared in activityOfTheSameDay) {
                        if (checkedActivities.contains(compared) ||
                                activity.start!!.isAfter(compared.end) || activity.start == compared.end ||
                                activity.end!!.isBefore(compared.start) || activity.end == compared.start) {
                            println("not clashed ${activity.id} and ${compared.id}")
                            continue
                        }
                        println("clashed ${activity.id} and ${compared.id}")
                        errors.add("Clash between activities: ${System.lineSeparator()}" +
                                "$activity ${System.lineSeparator()} in ${System.lineSeparator()}" +
                                "Module(id: ${currentModule.id}, name: ${currentModule.name})"+
                                "${System.lineSeparator()}and${System.lineSeparator()}" +
                                "$compared ${System.lineSeparator()}in${System.lineSeparator()}" +
                                "Module(id: ${moduleToCheck.id}, name: ${moduleToCheck.name})")
                    }
                }
                checkedActivities.add(activity)
            }
            println("finished module processing ${currentModule.id}")
            checkedModules.add(currentModule)
        }
        return errors
    }
}