package com.notilocations.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Repository for NotiLocations that gives access to the NotiLocations database.
 * @param app The application that is creating the repository.
 * @property taskDao The task database access object.
 * @property locationDao The location database access object.
 * @property locationTaskDao The location task database access object.
 * @property tasks The list of all tasks.
 * @property locations The list of all locations.
 * @property locationTasks The list of all location tasks.
 * @property fullLocationTasks The list of all full location tasks.
 * @property activeFullLocationTasks The list of all active full location tasks.
 * @property completedFullLocationTasks The list of all completed full location tasks.
 * @property executor A single thread executor for executing inserts, updates, and deletes in the database asynchronously.
 */
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

    /**
     * Runs when the class is created and initializes all the class variables with information from the database.
     */
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

    /**
     * Gets a live data list of all tasks.
     * @return A live data list of all tasks.
     */
    fun getTasks(): LiveData<List<Task>> {
        return tasks
    }

    /**
     * Gets a live data list of all locations
     * @return A live data list of all locations.
     */
    fun getLocations(): LiveData<List<Location>> {
        return locations
    }

    /**
     * Gets a live data list of all location tasks.
     * @return A live data list of all location tasks.
     */
    fun getLocationTasks(): LiveData<List<LocationTask>> {
        return locationTasks
    }

    /**
     * Gets a live data list of all full location tasks.
     * @return A live data list of all full location tasks.
     */
    fun getFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return fullLocationTasks
    }

    /**
     * Gets a live data list of all active full location tasks.
     * @return A live data list of all active full location tasks.
     */
    fun getActiveFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return activeFullLocationTasks
    }

    /**
     * Gets a live data list of all full location tasks.
     * @return A live data list of all full location tasks.
     */
    fun getFullLocationTasksStatic(): List<FullLocationTask> {
        return locationTaskDao.getFullLocationTasksStatic()
    }

    /**
     * Gets a live data list of all active full location tasks.
     * @return A live data list of all active full location tasks.
     */
    fun getActiveFullLocationTasksStatic(): List<FullLocationTask> {
        return locationTaskDao.getActiveFullLocationTasksStatic()
    }

    /**
     * Gets a live data list of all completed full location tasks.
     * @return A live data list of all completed full location tasks.
     */
    fun getCompletedFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return completedFullLocationTasks
    }

    /**
     * Gets the task with the given id.
     * @param id The id of the task to get.
     * @return A live data view of the task.
     */
    fun getTask(id: Long): LiveData<Task> {
        return taskDao.getTask(id)
    }

    /**
     * Gets the location with the given id.
     * @param id The id of the location to get.
     * @return A live data view of the location.
     */
    fun getLocation(id: Long): LiveData<Location> {
        return locationDao.getLocation(id)
    }

    /**
     * Gets the location task with the given id.
     * @param id The id of the location task to get.
     * @return A live data view of the location task.
     */
    fun getLocationTask(id: Long): LiveData<LocationTask> {
        return locationTaskDao.getLocationTask(id)
    }

    /**
     * Gets the full location task with the given id.
     * @param id The id of the full location task to get.
     * @return A live data view of the full location task.
     */
    fun getFullLocationTask(id: Long): LiveData<FullLocationTask> {
        return locationTaskDao.getFullLocationTask(id)
    }

    /**
     * Gets the full location task with the given id.
     * @param id The id of the full location task to get.
     * @return A static view of the full location task.
     */
    fun getFullLocationTaskStatic(id: Long): FullLocationTask {
        return locationTaskDao.getFullLocationTaskStatic(id)
    }

    /**
     * Creates a task asynchronously.
     * @param task The task to create.
     */
    fun createTask(task: Task) {
        executor.execute {
            taskDao.insertAll(task)
        }
    }

    /**
     * Deletes a task asynchronously.
     * @param task The task to delete.
     */
    fun deleteTask(task: Task) {
        executor.execute {
            taskDao.delete(task)
        }
    }

    /**
     * Updates a task asynchronously.
     * @param task The task to update.
     */
    fun updateTask(task: Task) {
        executor.execute {
            taskDao.update(task)
        }
    }

    /**
     * Creates a location asynchronously.
     * @param location The location to create.
     */
    fun createLocation(location: Location) {
        executor.execute {
            locationDao.insertAll(location)
        }
    }

    /**
     * Deletes a location asynchronously.
     * @param location The location to delete.
     */
    fun deleteLocation(location: Location) {
        executor.execute {
            locationDao.delete(location)
        }
    }

    /**
     * Updates a location asynchronously.
     * @param location The location to update.
     */
    fun updateLocation(location: Location) {
        executor.execute {
            locationDao.update(location)
        }
    }

    /**
     * Creates a location task asynchronously.
     * @param locationTask The location task to insert.
     */
    fun createLocationTask(locationTask: LocationTask) {
        executor.execute {
            locationTaskDao.insertAll(locationTask)
        }
    }

    /**
     * Deletes a location task asynchronously.
     * @param locationTask The location task to delete.
     */
    fun deleteLocationTask(locationTask: LocationTask) {
        executor.execute {
            locationTaskDao.delete(locationTask)
        }
    }

    /**
     * Updates a location task asynchronously.
     * @param locationTask The location task to update.
     */
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