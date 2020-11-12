package com.notilocations.database

import androidx.room.Embedded
import androidx.room.Relation

data class LocationWithTasks(
    @Embedded val location: Location,
    @Relation(
        entity = LocationTask::class,
        parentColumn = "id",
        entityColumn = "location_id"
    )
    val locationTasksWithTasks: List<LocationTaskWithTask>
)