package com.notilocations.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NotiLocationsRepository private constructor(app: Application) {

    private var taskDao: TaskDao
    private var locationDao: LocationDao
    private var locationTaskDao: LocationTaskDao

    private var tasks: LiveData<List<Task>>
    private var locations: LiveData<List<Location>>
    private var locationTasks: LiveData<List<LocationTask>>

    private var fullLocationTasks: LiveData<List<FullLocationTask>>
    private var activeFullLocationTasks: LiveData<List<FullLocationTask>>
    private var completedFullLocationTasks: LiveData<List<FullLocationTask>>

    private var executor: Executor

    init {
        val database: NotiLocationsDatabase = NotiLocationsDatabase.getInstance(app)
        taskDao = database.taskDao()
        locationDao = database.locationDao()
        locationTaskDao = database.locationTaskDao()

        tasks = taskDao.getTasks()
        locations = locationDao.getLocations()
        locationTasks = locationTaskDao.getLocationTasks()

        fullLocationTasks = locationTaskDao.getFullLocationTasks()
        activeFullLocationTasks = locationTaskDao.getActiveFullLocationTasks()
        completedFullLocationTasks = locationTaskDao.getCompletedFullLocationTasks()

        executor = Executors.newSingleThreadExecutor()
    }

    fun getTasks(): LiveData<List<Task>> {
        return tasks
    }

    fun getLocations(): LiveData<List<Location>> {
        return locations
    }

    fun getLocationTasks(): LiveData<List<LocationTask>> {
        return locationTasks
    }

    fun getFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return fullLocationTasks
    }

    fun getActiveFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return activeFullLocationTasks
    }

    fun getCompletedFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return completedFullLocationTasks
    }

    fun getLocation(id: Long): LiveData<Location> {
        return locationDao.getLocation(id)
    }

    fun getTask(id: Long): LiveData<Task> {
        return taskDao.getTask(id)
    }

    fun getLocationTask(id: Long): LiveData<LocationTask> {
        return locationTaskDao.getLocationTask(id)
    }

    fun getFullLocationTask(id: Long): LiveData<FullLocationTask> {
        return locationTaskDao.getFullLocationTask(id)
    }

    fun insertTask(task: Task) {
        executor.execute {
            taskDao.insertAll(task)
        }
    }

    fun deleteTask(task: Task) {
        executor.execute {
            taskDao.update(task)
        }
    }

    fun updateTask(task: Task) {
        executor.execute {
            taskDao.delete(task)
        }
    }

    fun insertLocation(location: Location) {
        executor.execute {
            locationDao.insertAll(location)
        }
    }

    fun deleteLocation(location: Location) {
        executor.execute {
            locationDao.delete(location)
        }
    }

    fun updateLocation(location: Location) {
        executor.execute {
            locationDao.update(location)
        }
    }

    fun insertLocationTask(locationTask: LocationTask) {
        executor.execute {
            locationTaskDao.insertAll(locationTask)
        }
    }

    fun deleteLocationTask(locationTask: LocationTask) {
        executor.execute {
            locationTaskDao.delete(locationTask)
        }
    }

    fun updateLocationTask(locationTask: LocationTask) {
        executor.execute {
            locationTaskDao.update(locationTask)
        }
    }


    /**
     * Companion object that returns an instance of the sub repository class, allowing it to be used as a Singleton.
     * @property instance Holds an instance of the sub repository after it is initialized by the first call to get the instance.
     */
    companion object {
        private var instance: NotiLocationsRepository? = null

        /**
         * Gets an instance of the sub repository.
         * @param app The application that is creating the repository.
         * @return An instance of the sub repository.
         */
        fun getInstance(app: Application): NotiLocationsRepository {
            if (instance == null) {
                instance = NotiLocationsRepository(app)
            }

            return instance as NotiLocationsRepository
        }
    }
}