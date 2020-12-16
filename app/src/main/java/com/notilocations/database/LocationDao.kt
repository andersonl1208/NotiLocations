package com.notilocations.database

import androidx.room.*

/**
 * Dao interface for locations.
 */
@Dao
interface LocationDao {

    /**
     * Inserts a variable number of locations into the database.
     * @param location The location to insert.
     */
    @Insert
    fun insert(location: Location?): Long

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

    /**
     * Deletes all locations without an associated location task from the database.
     */
    @Query("DELETE FROM location WHERE id NOT IN (SELECT location_id FROM locationtask)")
    fun cleanUpLocations()
}