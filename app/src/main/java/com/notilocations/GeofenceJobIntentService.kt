package com.notilocations

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.notilocations.database.FullLocationTask
import com.notilocations.database.NotiLocationsRepository

class GeofenceJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e("MyLogMessage", errorMessage)
            return
        }

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            val locationTasks = getTriggeredLocationTasks(triggeringGeofences)

            for (locationTask in locationTasks) {
                sendNotification(locationTask)
            }

        } else {
            Log.e("MyLogMessage", "Invalid geofence transition type")
        }
    }

    private fun getTriggeredLocationTasks(geofences: List<Geofence>): List<FullLocationTask> {
        val repository = NotiLocationsRepository.getInstance(application)

        val locationTasks = mutableListOf<FullLocationTask>()

        for (geofence in geofences) {
            try {
                val id = geofence.requestId.toLong()
                locationTasks.add(repository.getFullLocationTaskStatic(id))
            } catch (e: NumberFormatException) {
                Log.e("MyLogMessage", e.message ?: "Failed to cast string to a long")
            }


        }

        return locationTasks
    }

    private fun sendNotification(locationTask: FullLocationTask) {
        Log.i("MyLogMessage", "sendNotification: " + locationTask.task.title)
        val notificationBuilder =
            NotificationCompat.Builder(this, getString(R.string.primary_notification_channel_id))
                .setSmallIcon(R.drawable.ic_stat_temp_notification)
                .setContentTitle(locationTask.task.title)
                .setContentText(locationTask.task.description)
                .setStyle(NotificationCompat.BigTextStyle().bigText(locationTask.task.description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(locationTask.locationTask.id?.toInt() ?: 0, notificationBuilder.build())
        }

    }

    companion object {

        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }
}