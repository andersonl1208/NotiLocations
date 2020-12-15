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

class HandleGeofences private constructor(val context: Context) {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun create() {
        setupGeofences()
    }

    private fun setupGeofences() {
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                createGeofences()
            }
            addOnFailureListener {
                createGeofences()
            }
        }
    }

    private fun createGeofences() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("MyLogMessage", "Missing permission")
            return
        }

        Executors.newSingleThreadExecutor().execute {
            val repository =
                NotiLocationsRepository.getInstance(context.applicationContext as Application)

            val activeLocationTasks = repository.getActiveFullLocationTasksStatic()

            val geofences = mutableListOf<Geofence>()

            for (locationTask in activeLocationTasks) {
                val geofence = createGeofence(locationTask)
                if (geofence != null) {
                    geofences.add(geofence)
                }
            }

            if (geofences.size > 0) {
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
        }
    }

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

    private fun createGeofencingRequest(geofences: List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
//            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            setInitialTrigger(0)
            addGeofences(geofences)
        }.build()
    }

    companion object {
        private var instance: HandleGeofences? = null
        private var geofencesAdded: HandleGeofences? = null

        fun getInstance(context: Context): HandleGeofences {
            if (instance == null) {
                instance = HandleGeofences(context)
            }

            return instance as HandleGeofences
        }
    }
}