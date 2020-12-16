package com.notilocations

import android.Manifest
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.notilocations.database.FullLocationTask
import com.notilocations.database.NotiLocationsRepository
import java.util.concurrent.Executors

/**
 * Class to add and remove the geofences.
 * @property geofencingClient The location services geofencing client.
 * @property geofencePendingIntent The pending intent to use for the geofences.
 */
class HandleGeofences private constructor(val context: Context) {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Creates geofences and removes the old geofences.
     */
    fun create() {
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                setupGeofences()
            }
            addOnFailureListener {
                setupGeofences()
            }
        }
    }

    /**
     * Handles the setup and creation of the new geofences.
     */
    private fun setupGeofences() {

        Executors.newSingleThreadExecutor().execute {
            val repository =
                NotiLocationsRepository.getInstance(context.applicationContext as Application)
            val activeLocationTasks = repository.getActiveFullLocationTasksStatic()
            val geofences = createGeofences(activeLocationTasks)

            if (geofences.isNotEmpty()) {
                addGeofences(geofences)
            }
        }
    }

    /**
     * Creates a geofencing request with the given geofences and runs it.
     * @param geofences The geofences to add.
     */
    private fun addGeofences(geofences: List<Geofence>) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("MyLogMessage", "Missing permission")
            return
        }

        geofencingClient.addGeofences(
            createGeofencingRequest(geofences),
            geofencePendingIntent
        )
            .run {
                addOnSuccessListener {
                    Log.i("MyLogMessage", "Successfully added geofences")
                }
                addOnFailureListener {
                    Log.i("MyLogMessage", "Failed to add geofences")
                }
            }
    }

    /**
     * Creates geofences for each location task and returns the list of them.
     * @param locationTasks The location tasks to create geofences for.
     * @return The list of geofences that were created.
     */
    private fun createGeofences(locationTasks: List<FullLocationTask>): List<Geofence> {

        val geofences = mutableListOf<Geofence>()

        for (locationTask in locationTasks) {
            val geofence = createGeofence(locationTask)
            if (geofence != null) {
                geofences.add(geofence)
            }
        }

        return geofences
    }

    /**
     * Creates a geofence from a full location task.
     * @param fullLocationTask The full location task to create a geofence from.
     * @return The created geofence.
     */
    private fun createGeofence(fullLocationTask: FullLocationTask): Geofence? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val radius =
            (fullLocationTask.locationTask.distance ?: sharedPreferences.getString("distance", "")
                ?.toFloatOrNull() ?: 1.0F) * 1609.0F

        val transitionType = if (fullLocationTask.locationTask.triggerOnExit) {
            Geofence.GEOFENCE_TRANSITION_EXIT
        } else {
            Geofence.GEOFENCE_TRANSITION_ENTER
        }

        return Geofence.Builder()
            .setRequestId(fullLocationTask.locationTask.id.toString())
            .setCircularRegion(fullLocationTask.location.lat, fullLocationTask.location.lng, radius)
            .setTransitionTypes(transitionType)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    /**
     * Creates and builds a geofencing request with the list of geofences.
     * @param geofences The list of geofences to create the geofencing request for.
     * @return The geofencing request object that was created.
     */
    private fun createGeofencingRequest(geofences: List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            setInitialTrigger(0)
            addGeofences(geofences)
        }.build()
    }

    /**
     * Companion object that returns an instance of the HandleGeofences class, allowing it to be used as a Singleton.
     * @property instance Holds an instance of the HandleGeofences class after it is initialized by the first call to get the instance.
     */
    companion object {
        private var instance: HandleGeofences? = null

        /**
         * Gets an instance of the HandleGeofences class.
         * @param context The context the class is being created in.
         * @return An instance of the HandleGeofences class.
         */
        fun getInstance(context: Context): HandleGeofences {
            if (instance == null) {
                instance = HandleGeofences(context)
            }

            return instance as HandleGeofences
        }
    }
}