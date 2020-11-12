package com.notilocations.database

import androidx.room.Embedded
import androidx.room.Relation

data class LocationTaskWithLocation(
    @Embedded val locationTask: LocationTask,
    @Relation(
        parentColumn = "location_id",
        entityColumn = "id"
    )
    val Location: Location
)