package com.notilocations.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationDao {

    @Query("SELECT * from location WHERE id = :id")
    fun getLocation(id: Long): LiveData<Location>

    @Query("SELECT * from location")
    fun getLocations(): LiveData<List<Location>>

    @Transaction
    @Query("SELECT * from location")
    fun getLocationsWithTasks(): LiveData<List<LocationWithTasks>>

    @Transaction
    @Query("SELECT * from location WHERE id = :id")
    fun getLocationWithTasks(id: Long): LiveData<LocationWithTasks>

    @Query("SELECT DISTINCT location.* FROM location")
    fun getActiveLocations(): LiveData<List<Location>>

    @Insert
    fun insertAll(vararg location: Location?)

    @Delete
    fun delete(location: Location?)

    @Update
    fun update(location: Location?)
}