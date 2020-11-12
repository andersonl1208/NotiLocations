package com.notilocations.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationTaskDao {
    @Query("SELECT * FROM locationtask WHERE id = :id")
    fun getLocationTask(id: Long): LiveData<LocationTask>

    @Query("SELECT * FROM locationtask")
    fun getLocationTasks(): LiveData<List<LocationTask>>

    @Query("SELECT * FROM locationtask WHERE id = :id")
    fun getFullLocationTask(id: Long): LiveData<FullLocationTask>

    @Query("SELECT * FROM locationtask")
    fun getFullLocationTasks(): LiveData<List<FullLocationTask>>

    @Query("SELECT * FROM locationtask WHERE NOT is_completed")
    fun getActiveFullLocationTasks(): LiveData<List<FullLocationTask>>

    @Query("SELECT * FROM locationtask WHERE is_completed")
    fun getCompletedFullLocationTasks(): LiveData<List<FullLocationTask>>

    @Insert
    fun insertAll(vararg locationTask: LocationTask?)

    @Delete
    fun delete(locationTask: LocationTask?)

    @Update
    fun update(locationTask: LocationTask?)
}