package com.notilocations.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * from task WHERE id = :id")
    fun getTask(id: Long): LiveData<Task>

    @Query("SELECT * from task")
    fun getTasks(): LiveData<List<Task>>

    @Insert
    fun insertAll(vararg task: Task?)

    @Delete
    fun delete(task: Task?)

    @Update
    fun update(task: Task?)
}