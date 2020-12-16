package com.notilocations.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.notilocations.NotiLocationTask
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Repository for NotiLocations that gives access to the NotiLocations database.
 * @param app The application that is creating the repository.
 * @property taskDao The task database access object.
 * @property locationDao The location database access object.
 * @property locationTaskDao The location task database access object.
 * @property activeFullLocationTasks The list of all active full location tasks.
 * @property executor A single thread executor for executing inserts, updates, and deletes in the database asynchronously.
 */
class NotiLocationsRepository private constructor(app: Application) {

    private var taskDao: TaskDao
    private var locationDao: LocationDao
    private var locationTaskDao: LocationTaskDao

    private var activeFullLocationTasks: LiveData<List<FullLocationTask>>

    private var executor: Executor

    /**
     * Runs when the class is created and initializes all the class variables with information from the database.
     */
    init {
        val database: NotiLocationsDatabase = NotiLocationsDatabase.getInstance(app)
        taskDao = database.taskDao()
        locationDao = database.locationDao()
        locationTaskDao = database.locationTaskDao()

        activeFullLocationTasks = locationTaskDao.getActiveFullLocationTasks()

        executor = Executors.newSingleThreadExecutor()
    }

    /**
     * Gets a live data list of all active full location tasks.
     * @return A live data list of all active full location tasks.
     */
    fun getActiveFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return activeFullLocationTasks
    }

    /**
     * Gets a static list of all active full location tasks. Must be called from a background thread.
     * @return A live data list of all active full location tasks.
     */
    fun getActiveFullLocationTasksStatic(): List<FullLocationTask> {
        return locationTaskDao.getActiveFullLocationTasksStatic()
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
     * Creates a location task asynchronously.
     * @param locationTask The location task to insert.
     */
    fun createLocationTask(locationTask: LocationTask) {
        executor.execute {
            locationTaskDao.insert(locationTask)
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
     * Syncs a NotiLocationTask with the database, inserting or updating as appropriate based on whether ids have been set.
     * @param notiLocationTask The NotiLocation task to sync.
     */
    fun syncNotiLocationTask(notiLocationTask: NotiLocationTask) {
        executor.execute {
            if (notiLocationTask.hasLocationId()) {
                locationDao.update(notiLocationTask.getDatabaseLocation())
            } else {
                notiLocationTask.location?.locationId =
                    locationDao.insert(notiLocationTask.getDatabaseLocation())
            }

            if (notiLocationTask.hasTaskId()) {
                taskDao.update(notiLocationTask.getDatabaseTask())
            } else {
                notiLocationTask.task?.taskId = taskDao.insert(notiLocationTask.getDatabaseTask())
            }

            if (notiLocationTask.hasLocationTaskId()) {
                locationTaskDao.update(notiLocationTask.getDatabaseLocationTask())
            } else {
                locationTaskDao.insert(notiLocationTask.getDatabaseLocationTask())
            }
        }
    }

    /**
     * Cleans up the repository.
     */
    fun cleanUp() {
        executor.execute {
            locationDao.cleanUpLocations()
            taskDao.cleanUpTasks()
        }
    }

    /**
     * Companion object that returns an instance of the NotiLocation repository class, allowing it to be used as a Singleton.
     * @property instance Holds an instance of the NotiLocation repository after it is initialized by the first call to get the instance.
     */
    companion object {
        private var instance: NotiLocationsRepository? = null

        /**
         * Gets an instance of the NotiLocation repository.
         * @param app The application that is creating the repository.
         * @return An instance of the NotiLocation repository.
         */
        fun getInstance(app: Application): NotiLocationsRepository {
            if (instance == null) {
                instance = NotiLocationsRepository(app)
            }

            return instance as NotiLocationsRepository
        }
    }
}