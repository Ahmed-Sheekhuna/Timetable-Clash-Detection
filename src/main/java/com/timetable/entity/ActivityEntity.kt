package com.timetable.entity

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

//entity annotation is for marking class as a object persisted in DB, name defines table name created in DB
//table in db will automatically create
@Entity(name = "activity")
class ActivityEntity(
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var name: String? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var type: String? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var start: LocalTime? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var end: LocalTime? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var day: DayOfWeek? = null,
        //id marks colums as primary key and generatedValue defines type of id generation, i.e. 1, 2, 3, 4, etc.
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null)
