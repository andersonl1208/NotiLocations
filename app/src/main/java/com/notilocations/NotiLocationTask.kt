package com.notilocations

import android.os.Parcelable
import com.notilocations.database.FullLocationTask
import com.notilocations.database.Location
import com.notilocations.database.LocationTask
import com.notilocations.database.Task
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Parcelable version of a full location task.
 * @property locationTaskId The id of the location task.
 * @property distance The radius of the circle in which the notification will be triggered.
 * @property maxSpeed The max speed you can be going while still getting a notification.
 * @property creationDate The date the location task was created.
 * @property isCompleted Whether or not this location task has been completed.
 * @property location The location associated with this location task.
 * @property task The task associated with this location task.
 */
@Parcelize
data class NotiLocationTask(
    var locationTaskId: Long? = null,
    var distance: Float? = null,
    var maxSpeed: Int? = null,
    var creationDate: Date = Date(),
    var isCompleted: Boolean = false,
    var triggerOnExit: Boolean = false,

    var location: NotiLocation? = null,
    var task: NotiTask? = null
) : Parcelable {

    /**
     * Returns true if the NotiLocationTask has a location.
     * @return True if the location is null, false otherwise.
     */
    fun hasLocation(): Boolean {
        return location != null
    }

    /**
     * Returns true if the NotiLocationTask has a task.
     * @return True if the task is null, false otherwise.
     */
    fun hasTask(): Boolean {
        return task != null
    }

    /**
     * Returns true if the location has an id.
     * @return True if the location has an id, false otherwise.
     */
    fun hasLocationId(): Boolean {
        return location?.hasId() ?: false
    }

    /**
     * Returns true if the task has an id.
     * @return True if the task has an id, false otherwise.
     */
    fun hasTaskId(): Boolean {
        return task?.hasId() ?: false
    }

    /**
     * Returns true if this NotiLocationTask has an id set.
     * @return Whether or not the location task id is null.
     */
    fun hasLocationTaskId(): Boolean {
        return locationTaskId != null
    }

    /**
     * Returns a database version of the location associated with this location task if it is not null.
     * @return A database class version of the location.
     */
    fun getDatabaseLocation(): Location? {
        if (location != null) {
            return Location(location!!.locationId, location!!.name, location!!.lat, location!!.lng)
        }

        return null
    }

    /**
     * Returns a database version of the task associated with this location task if it is not null.
     * @return A database class version of the task.
     */
    fun getDatabaseTask(): Task? {
        if (task != null) {
            return Task(task!!.taskId, task!!.title, task!!.description)
        }

        return null
    }

    /**
     * Returns a database version of this LocationTask if the location and task ids are set.
     * @return A database class version of the location task.
     */
    fun getDatabaseLocationTask(): LocationTask? {
        if (hasLocationId() && hasTaskId()) {
            return LocationTask(
                locationTaskId,
                location!!.locationId!!,
                task!!.taskId!!,
                distance,
                maxSpeed,
                creationDate,
                isCompleted,
                triggerOnExit
            )
        }

        return null
    }

    /**
     * Companion object to NotiLocationTask, which allows one to be created from a full location task.
     */
    companion object {

        /**
         * Creates a NotiLocationTask from a FullLocationTask.
         * @param fullLocationTask The fullLocationTask to use as a blueprint.
         * @return A NotiLocationTask with the same data as the FullLocationTask.
         */
        fun createFromFullLocationTask(fullLocationTask: FullLocationTask?): NotiLocationTask? {
            if (fullLocationTask != null) {
                val location = NotiLocation(
                    fullLocationTask.location.id,
                    fullLocationTask.location.name,
                    fullLocationTask.location.lat,
                    fullLocationTask.location.lng
                )

                val task = NotiTask(
                    fullLocationTask.task.id,
                    fullLocationTask.task.title,
                    fullLocationTask.task.description
                )

                return NotiLocationTask(
                    fullLocationTask.locationTask.id,
                    fullLocationTask.locationTask.distance,
                    fullLocationTask.locationTask.maxSpeed,
                    fullLocationTask.locationTask.creationDate,
                    fullLocationTask.locationTask.isCompleted,
                    fullLocationTask.locationTask.triggerOnExit,
                    location,
                    task
                )
            }

            return null
        }
    }
}