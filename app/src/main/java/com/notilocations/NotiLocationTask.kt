package com.notilocations

import android.os.Parcelable
import com.notilocations.database.FullLocationTask
import com.notilocations.database.Location
import com.notilocations.database.LocationTask
import com.notilocations.database.Task
import kotlinx.android.parcel.Parcelize
import java.util.*

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

    fun hasLocation(): Boolean {
        return location != null
    }

    fun hasTask(): Boolean {
        return task != null
    }

    fun hasLocationId(): Boolean {
        return location?.hasId() ?: false
    }

    fun hasTaskId(): Boolean {
        return task?.hasId() ?: false
    }

    fun hasLocationTaskId(): Boolean {
        return locationTaskId != null
    }

    fun getDatabaseLocation(): Location? {
        if (location != null) {
            return Location(location!!.locationId, location!!.name, location!!.lat, location!!.lng)
        }

        return null
    }

    fun getDatabaseTask(): Task? {
        if (task != null) {
            return Task(task!!.taskId, task!!.title, task!!.description)
        }

        return null
    }

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

    companion object {

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