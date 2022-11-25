package com.timetable.entity

import jakarta.persistence.*

//entity annotation is for marking class as a object persisted in DB, name defines table name created in DB
//table in db will automatically create
@Entity(name = "term")
class TermEntity(
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var termNumber: Int? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var startMonth: Int? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var endMonth: Int? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = true)
        //one to many maps entities between each other, fetch type - eager is to load this data together with main entity
        @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
        var modules: List<ModuleEntity>? = null,
        //column annotation is to mark class field as table field stored in db
        @Column(nullable = false)
        var year: Int? = null,
        //id marks colums as primary key and generatedValue defines type of id generation, i.e. 1, 2, 3, 4, etc.
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null)