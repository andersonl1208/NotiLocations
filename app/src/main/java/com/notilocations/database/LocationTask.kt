package com.notilocations.database

import androidx.room.*
import java.util.*

@Entity(
    indices = [Index(value = ["location_id"]), Index(value = ["task_id"])]
)
data class LocationTask(
    @PrimaryKey val id: Long?,
    @ForeignKey(
        entity = Location::class,
        parentColumns = ["id"],
        childColumns = ["location_id"],
        onDelete = ForeignKey.CASCADE
    )
    @ColumnInfo(name = "location_id")
    val locationId: Long,

    @ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],
        childColumns = ["task_id"],
        onDelete = ForeignKey.CASCADE
    )
    @ColumnInfo(name = "task_id")
    val taskId: Long,

    @ColumnInfo(name = "distance")
    val distance: Double,

    @ColumnInfo(name = "max_speed")
    val maxSpeed: Int,

    @ColumnInfo(name = "creation_date")
    val creationDate: Date,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean
)