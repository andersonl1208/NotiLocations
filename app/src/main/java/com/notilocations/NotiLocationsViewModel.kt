package com.notilocations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.notilocations.database.*

/**
 * The NotiLocations view model that links the NotiLocations repository to the views.
 *
 * @param app The application creating the class.
 * @property repository A reference to the NotiLocations repository.
 * @property tasks The list of all tasks.
 * @property locations The list of all locations.
 * @property locationTasks The list of all location tasks.
 * @property fullLocationTasks The list of all full location tasks.
 * @property activeFullLocationTasks The list of all active full location tasks.
 * @property completedFullLocationTasks The list of all completed full location tasks.
 */
class NotiLocationsViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: NotiLocationsRepository = NotiLocationsRepository.getInstance(app)

    private val tasks: LiveData<List<Task>> = repository.getTasks()
    private val locations: LiveData<List<Location>> = repository.getLocations()
    private val locationTasks: LiveData<List<LocationTask>> = repository.getLocationTasks()

    private val fullLocationTasks: LiveData<List<FullLocationTask>> =
        repository.getFullLocationTasks()
    private val activeFullLocationTasks: LiveData<List<FullLocationTask>> =
        repository.getActiveFullLocationTasks()
    private val completedFullLocationTasks: LiveData<List<FullLocationTask>> =
        repository.getCompletedFullLocationTasks()

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

    fun getFullLocationTasksStatic(): List<FullLocationTask> {
        return repository.getFullLocationTasksStatic()
    }

    fun getActiveFullLocationTasksStatic(): List<FullLocationTask> {
        return repository.getActiveFullLocationTasksStatic()
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
        return repository.getTask(id)
    }

    /**
     * Gets the location with the given id.
     * @param id The id of the location to get.
     * @return A live data view of the location.
     */
    fun getLocation(id: Long): LiveData<Location> {
        return repository.getLocation(id)
    }

    /**
     * Gets the location task with the given id.
     * @param id The id of the location task to get.
     * @return A live data view of the location task.
     */
    fun getLocationTask(id: Long): LiveData<LocationTask> {
        return repository.getLocationTask(id)
    }

    /**
     * Gets the full location task with the given id.
     * @param id The id of the full location task to get.
     * @return A live data view of the full location task.
     */
    fun getFullLocationTask(id: Long): LiveData<FullLocationTask> {
        return repository.getFullLocationTask(id)
    }

    /**
     * Gets the full location task with the given id.
     * @param id The id of the full location task to get.
     * @return A static view of the full location task.
     */
    fun getFullLocationTaskStatic(id: Long): FullLocationTask {
        return repository.getFullLocationTaskStatic(id)
    }

    /**
     * Creates a task.
     * @param task The task to create.
     */
    fun createTask(task: Task) {
        repository.createTask(task)
    }

    /**
     * Deletes a task.
     * @param task The task to delete.
     */
    fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }

    /**
     * Updates a task.
     * @param task The task to update.
     */
    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    /**
     * Creates a location.
     * @param location The location to create.
     */
    fun createLocation(location: Location) {
        repository.createLocation(location)
    }

    /**
     * Deletes a location.
     * @param location The location to delete.
     */
    fun deleteLocation(location: Location) {
        repository.deleteLocation(location)
    }

    /**
     * Updates a location.
     * @param location The location to update.
     */
    fun updateLocation(location: Location) {
        repository.updateLocation(location)
    }

    /**
     * Creates a location task.
     * @param locationTask The location task to insert.
     */
    fun createLocationTask(locationTask: LocationTask) {
        repository.createLocationTask(locationTask)
    }

    /**
     * Deletes a location task.
     * @param locationTask The location task to delete.
     */
    fun deleteLocationTask(locationTask: LocationTask) {
        repository.deleteLocationTask(locationTask)
    }

    /**
     * Updates a location task.
     * @param locationTask The location task to update.
     */
    fun updateLocationTask(locationTask: LocationTask) {
        repository.updateLocationTask(locationTask)
    }
}