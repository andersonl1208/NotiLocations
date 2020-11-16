package com.notilocations.database

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Holds a location task and the associated task and location objects.
 * @property locationTask The location task
 * @property task The task associated with the location task
 * @property location The location associated with the location task
 */
data class FullLocationTask(
    @Embedded
    val locationTask: LocationTask,

    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: Task,

    @Relation(
        parentColumn = "location_id",
        entityColumn = "id"
    )
    val location: Location
)