package com.notilocations.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Dao interface for tasks. Contains functions to get a task, get all task, and insert, delete or update tasks.
 */
@Dao
interface TaskDao {

    /**
     * Gets the task with the given id.
     * @param id The id of the task to get.
     * @return A live data view of the task.
     */
    @Query("SELECT * from task WHERE id = :id")
    fun getTask(id: Long): LiveData<Task>

    /**
     * Gets a list of all the tasks in the database.
     * @return A live data list of all the tasks in the database.
     */
    @Query("SELECT * from task")
    fun getTasks(): LiveData<List<Task>>

    /**
     * Inserts a variable number of tasks into the database.
     * @param task A variable number of tasks to insert.
     */
    @Insert
    fun insert(task: Task?): Long

    /**
     * Deletes a task from the database.
     * @param task The task to delete.
     */
    @Delete
    fun delete(task: Task?)

    /**
     * Updates a task in the database.
     * @param task The task to update.
     */
    @Update
    fun update(task: Task?)
}