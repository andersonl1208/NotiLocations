package com.notilocations.database

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithLocations(
    @Embedded val task: Task,
    @Relation(
        entity = LocationTask::class,
        parentColumn = "id",
        entityColumn = "location_id"
    )
    val locationTasksWithLocations: List<LocationTaskWithLocation>
)