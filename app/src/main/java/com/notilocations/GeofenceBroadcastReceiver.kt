package com.notilocations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.notilocations.database.FullLocationTask
import com.notilocations.database.NotiLocationsRepository
import com.notilocations.database.Task

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e("NotiLocations", errorMessage)
            return
        }

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            val tasks: List<Task> = getTasks(triggeringGeofences)

            if (context != null) {
                for (task in tasks) {
                    sendNotification(context, task)
                }
            } else {
                Log.e("NotiLocations", "Context in GeofenceBroadcastReceiver onReceive was null")
            }

        } else {
            Log.e("NotiLocations", "Invalid geofence transition type")
        }
    }

    private fun getTriggeredLocationTasks(
        context: Context,
        geofences: List<Geofence>
    ): List<FullLocationTask> {
        val viewModel = NotiLocationsRepository.getInstance(application)

        val locationTasks = mutableListOf<FullLocationTask>()

        for (geofence in geofences) {
            val id: Long = geofence.requestId()
            val fullLocationTask = viewModel.getFullLocationTask(id)

            val currentLocationTask = fullLocationTask.value
            if (currentLocationTask != null) {
                locationTasks.add(currentLocationTask)
            }
        }

        return locationTasks
    }

    private fun sendNotification(context: Context, task: Task) {
        Log.i("NotiLocations", "sendNotification: " + task.title)
        NotificationCompat.Builder(
            context,
            context.getString(R.string.primary_notification_channel_id)
        )
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setStyle(NotificationCompat.BigTextStyle().bigText(task.description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
}