package com.notilocations.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Dao interface for locations. Contains functions to get a location, get all locations, and insert, delete or update locations.
 */
@Dao
interface LocationDao {

    /**
     * Gets the location with the given id.
     * @param id The id of the location to get.
     * @return A live data view of the location.
     */
    @Query("SELECT * from location WHERE id = :id")
    fun getLocation(id: Long): LiveData<Location>

    /**
     * Gets a list of all the locations in the database.
     * @return A live data list of all the locations in the database.
     */
    @Query("SELECT * from location")
    fun getLocations(): LiveData<List<Location>>

    /**
     * Inserts a variable number of locations into the database.
     * @param location A variable number of locations to insert.
     */
    @Insert
    fun insertAll(vararg location: Location?)

    /**
     * Deletes a location from the database.
     * @param location The location to delete.
     */
    @Delete
    fun delete(location: Location?)

    /**
     * Updates a location in the database.
     * @param location The location to update.
     */
    @Update
    fun update(location: Location?)
}