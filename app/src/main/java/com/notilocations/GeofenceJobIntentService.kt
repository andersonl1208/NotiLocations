package com.notilocations

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.speech.tts.TextToSpeech
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
import java.util.*

/**
 * Handles a geofence intent.
 */
class GeofenceJobIntentService : JobIntentService() {

    private var mTextToSpeech: TextToSpeech? = null

    /**
     * Takes a geofence intent and sends notifications for the associated location tasks.
     * @param intent The geofencing event intent.
     */
    override fun onHandleWork(intent: Intent) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e("MyLogMessage", errorMessage)
            return
        }

        val locationTasks = getTriggeredLocationTasks(geofencingEvent.triggeringGeofences)

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

        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { location: Location? ->
            locationRetrieved(location, locationTasks, sharedPreferences)
        }.addOnFailureListener {
            Log.e("MyLogMessage", "Last location failed to be retrieved")
        }
    }

    /**
     * When the current location is retrieved, runs through each location task and sends a notification if the current speed is less than the location tasks's max speed.
     * @param location The current location of the user.
     * @param locationTasks The list of location tasks to send notifications for.
     * @param sharedPreferences The shared preferences object to get the preferences from.
     */
    private fun locationRetrieved(
        location: Location?,
        locationTasks: List<FullLocationTask>,
        sharedPreferences: SharedPreferences
    ) {
        val currentSpeed = location?.speed ?: 0.0f
        val defaultMaxSpeed =
            if (sharedPreferences.getBoolean("max_speed_enabled", false)) {
                sharedPreferences.getFloat("max_speed", Float.MAX_VALUE)
            } else {
                Float.MAX_VALUE
            }

        for (locationTask in locationTasks) {

            val maxSpeed: Float =
                locationTask.locationTask.distance ?: defaultMaxSpeed

            if (currentSpeed * METERS_SEC_TO_MILES_HOUR < maxSpeed) {
                sendNotification(locationTask, sharedPreferences)
            }
        }
    }

    /**
     * Gets a list of the location tasks associated with the triggered geofences.
     * @param geofences The geofences that were triggered
     * @return The list of location tasks associated with the triggered geofences.
     */
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

    private fun sayText(context: Context?, message: String?) {
        mTextToSpeech = TextToSpeech(context) { status ->
            try {
                if (mTextToSpeech != null && status == TextToSpeech.SUCCESS) {
                    mTextToSpeech!!.language = Locale.US
                    mTextToSpeech!!.speak(message, TextToSpeech.QUEUE_ADD, null)
                }
            } catch (ex: Exception) {
                print("Error handling TextToSpeech GCM notification " + ex.message)
            }
        }
    }

    /**
     * Sends a notification for the location task and reads it aloud if enabled.
     * @param locationTask The location task to send a notification for.
     * @param sharedPreferences The shared preferences object to get the preferences from.
     */
    private fun sendNotification(
        locationTask: FullLocationTask,
        sharedPreferences: SharedPreferences
    ) {
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

        if (sharedPreferences.getBoolean("voice_notification", false)) {
            sayText(this, locationTask.task.title)
        }

    }

    /**
     * Companion object that holds constants and allows work to be queued.
     */
    companion object {

        /** Unique job id */
        private const val JOB_ID = 573

        /** Ratio to use to convert meters/sec to miles/hour */
        private const val METERS_SEC_TO_MILES_HOUR = 2.23694

        /**
         * Queues work for this class.
         * @param context The context of the work.
         * @param intent The intent of the work.
         */
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }
}