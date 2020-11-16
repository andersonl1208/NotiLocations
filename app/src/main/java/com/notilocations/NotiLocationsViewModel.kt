package com.notilocations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.notilocations.database.*

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
        return repository.getLocation(id)
    }

    fun getTask(id: Long): LiveData<Task> {
        return repository.getTask(id)
    }

    fun getLocationTask(id: Long): LiveData<LocationTask> {
        return repository.getLocationTask(id)
    }

    fun getFullLocationTask(id: Long): LiveData<FullLocationTask> {
        return repository.getFullLocationTask(id)
    }

    fun insertTask(task: Task) {
        repository.insertTask(task)
    }

    fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun insertLocation(location: Location) {
        repository.insertLocation(location)
    }

    fun deleteLocation(location: Location) {
        repository.deleteLocation(location)
    }

    fun updateLocation(location: Location) {
        repository.updateLocation(location)
    }

    fun insertLocationTask(locationTask: LocationTask) {
        repository.insertLocationTask(locationTask)
    }

    fun deleteLocationTask(locationTask: LocationTask) {
        repository.deleteLocationTask(locationTask)
    }

    fun updateLocationTask(locationTask: LocationTask) {
        repository.updateLocationTask(locationTask)
    }
}