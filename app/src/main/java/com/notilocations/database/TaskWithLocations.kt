package com.notilocations.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TaskWithLocations(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            LocationTask::class,
            parentColumn = "task_id",
            entityColumn = "location_id"
        )
    )
    val locations: List<Location>
)