package com.notilocations.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * from task WHERE id = :id")
    fun getTask(id: Long): LiveData<Task>

    @Query("SELECT * from task")
    fun getTasks(): LiveData<List<Task>>

    @Transaction
    @Query("SELECT * from task")
    fun getTasksWithLocations(): LiveData<List<TaskWithLocations>>

    @Transaction
    @Query("SELECT * from task WHERE id = :id")
    fun getTaskWithLocations(id: Long): LiveData<TaskWithLocations>

    @Query("SELECT * FROM task")
    fun getActiveTasks(): LiveData<List<Task>>

    @Transaction
    @Query("SELECT * FROM task")
    fun getActiveTasksWithLocations(): LiveData<List<TaskWithLocations>>

    @Insert
    fun insertAll(vararg task: Task?)

    @Delete
    fun delete(task: Task?)

    @Update
    fun update(task: Task?)
}