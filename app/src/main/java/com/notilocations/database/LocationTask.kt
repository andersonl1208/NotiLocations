package com.notilocations.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["location_id", "task_id"],
    indices = [Index(value = ["location_id"]), Index(value = ["task_id"])]
)
data class LocationTask(
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
    val taskId: Long
)