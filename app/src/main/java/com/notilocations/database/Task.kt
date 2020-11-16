package com.notilocations.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class for a task.
 * @property id The primary key for the task. If null when inserted, it will be auto-generated.
 * @property title The title of the task.
 * @property description The description of the task.
 */
@Entity
data class Task(
    @PrimaryKey val id: Long?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String?
)