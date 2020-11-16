package com.notilocations.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class for a location.
 * @property id The primary key for the location. If null when inserted, it will be auto-generated.
 * @property name The name of the location.
 * @property lat The latitude of the location.
 * @property lng The longitude of the location.
 */
@Entity
data class Location(
    @PrimaryKey val id: Long?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lng")
    val lng: Double
)