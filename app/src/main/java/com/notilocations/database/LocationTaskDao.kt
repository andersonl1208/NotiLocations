package com.notilocations.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Dao interface for location tasks.
 */
@Dao
interface LocationTaskDao {

    /**
     * Gets the full location task with the given id, which contains the location task with the associated location
     * and task objects included.
     * @param id The id of the location task to get.
     * @return A static view of the full location task.
     */
    @Query("SELECT * FROM locationtask WHERE id = :id")
    fun getFullLocationTaskStatic(id: Long): FullLocationTask

    /**
     * Gets a list of all the locations tasks in the database with the is_completed field set to false as full
     * location tasks with the associated location and task objects included.
     * @return A live data list of all the active location tasks with location and task objects included in the database.
     */
    @Query("SELECT * FROM locationtask WHERE NOT is_completed")
    fun getActiveFullLocationTasks(): LiveData<List<FullLocationTask>>

    /**
     * Gets a list of all the locations tasks in the database with the is_completed field set to false as full
     * location tasks with the associated location and task objects included.
     * @return A static list of all the active location tasks with location and task objects included in the database.
     */
    @Query("SELECT * FROM locationtask WHERE NOT is_completed")
    fun getActiveFullLocationTasksStatic(): List<FullLocationTask>


    /**
     * Inserts a variable number of location tasks into the database.
     * @param locationTask A variable number of location tasks to insert.
     */
    @Insert
    fun insert(locationTask: LocationTask?): Long

    /**
     * Deletes a location task from the database.
     * @param locationTask The location task to delete.
     */
    @Delete
    fun delete(locationTask: LocationTask?)

    /**
     * Updates a location task in the database.
     * @param locationTask The location task to update.
     */
    @Update
    fun update(locationTask: LocationTask?)
}