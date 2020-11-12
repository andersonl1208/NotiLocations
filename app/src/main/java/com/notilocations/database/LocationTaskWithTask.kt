package com.notilocations.database

import androidx.room.Embedded
import androidx.room.Relation

data class LocationTaskWithTask(
    @Embedded
    val locationTask: LocationTask,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: Task
)