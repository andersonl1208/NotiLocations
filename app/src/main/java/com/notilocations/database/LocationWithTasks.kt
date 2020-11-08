package com.notilocations.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LocationWithTasks(
    @Embedded val location: Location,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            LocationTask::class,
            parentColumn = "location_id",
            entityColumn = "task_id"
        )
    )
    val tasks: List<Task>
)