package com.notilocations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.notilocations.database.FullLocationTask
import com.notilocations.database.LocationTask
import com.notilocations.database.NotiLocationsRepository

/**
 * The NotiLocations view model that links the NotiLocations repository to the views.
 *
 * @param app The application creating the class.
 * @property repository A reference to the NotiLocations repository.
 * @property activeFullLocationTasks The list of all active full location tasks.
 */
class NotiLocationsViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: NotiLocationsRepository = NotiLocationsRepository.getInstance(app)

    private val activeFullLocationTasks: LiveData<List<FullLocationTask>> =
        repository.getActiveFullLocationTasks()

    /**
     * Gets a live data list of all active full location tasks.
     * @return A live data list of all active full location tasks.
     */
    fun getActiveFullLocationTasks(): LiveData<List<FullLocationTask>> {
        return activeFullLocationTasks
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

    /**
     * Syncs a parcelable NotiLocationTask with the database.
     * @param notiLocationTask The NotiLocationTask to sync
     */
    fun syncNotiLocationTask(notiLocationTask: NotiLocationTask) {
        repository.syncNotiLocationTask(notiLocationTask)
    }

    /**
     * Cleans up the repository.
     */
    fun cleanUp() {
        repository.cleanUp()
    }
}