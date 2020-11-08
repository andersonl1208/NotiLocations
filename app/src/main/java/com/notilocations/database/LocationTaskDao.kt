package com.notilocations.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface LocationTaskDao {
    @Insert
    fun insertAll(vararg locationTask: LocationTask?)

    @Delete
    fun delete(locationTask: LocationTask?)

    @Update
    fun update(locationTask: LocationTask?)
}