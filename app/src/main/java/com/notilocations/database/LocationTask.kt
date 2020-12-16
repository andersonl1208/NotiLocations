package com.notilocations.database

import androidx.room.*
import java.util.*

/**
 * Entity class for a location task.
 * @property id The primary key for the location task. If null when inserted, it will be auto-generated.
 * @property locationId The id of the location associated with this location task.
 * @property taskId The id of the task associated with this location task.
 * @property distance The radius of the circle in which the notification will be triggered.
 * @property maxSpeed The max speed you can be going while still getting a notification.
 * @property creationDate The date the location task was created.
 * @property isCompleted Whether or not this location task has been completed.
 */
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
    val distance: Float?,

    @ColumnInfo(name = "max_speed")
    val maxSpeed: Int?,

    @ColumnInfo(name = "creation_date")
    val creationDate: Date,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,

    @ColumnInfo(name = "trigger_on_exit")
    val triggerOnExit: Boolean
)