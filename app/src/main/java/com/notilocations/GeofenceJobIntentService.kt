package com.notilocations

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.LocationServices
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

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("MyLogMessage", "Missing permissions")
                return
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null && location.hasSpeed()) {

                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

                    val defaultMaxSpeed =
                        if (sharedPreferences.getBoolean("max_speed_enabled", false)) {
                            sharedPreferences.getFloat("max_speed", Float.MAX_VALUE)
                        } else {
                            Float.MAX_VALUE
                        }

                    for (locationTask in locationTasks) {

                        val maxSpeed: Float =
                            locationTask.locationTask.distance ?: defaultMaxSpeed

                        if (location.speed * METERS_SEC_TO_MILES_HOUR < maxSpeed) {
                            sendNotification(locationTask)
                        }
                    }

                } else {
                    for (locationTask in locationTasks) {
                        sendNotification(locationTask)
                    }
                }
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
        private const val METERS_SEC_TO_MILES_HOUR = 2.23694

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }
}