package com.notilocations

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.notilocations.database.FullLocationTask
import com.notilocations.databinding.ActivityMainBinding
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    lateinit var geofencingClient: GeofencingClient

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        geofencingClient = LocationServices.getGeofencingClient(this)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (sharedPreferences?.getBoolean("dark_theme", false) == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

//        val navController = this.findNavController(R.id.navHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)

        createNotificationChannel()

        //val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

        //viewModel.createLocation(Location(45, "Test location", 44.872978, -91.929399))
        //viewModel.createTask(Task(45, "Test notification", "Test longer description. I don't know what I'm doing but it seems promising!"))
        //viewModel.createLocationTask(LocationTask(45, 45, 45, 500.0F, 100, Date(), false))

        val response =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (response != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1)
                .show()
        } else {
            setupGeofences()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.primary_notification_channel_name)
            val descriptionText = getString(R.string.primary_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                getString(R.string.primary_notification_channel_id),
                name,
                importance
            ).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
                this,
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
            val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

            val activeLocationTasks = viewModel.getActiveFullLocationTasksStatic()

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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val radius = fullLocationTask.locationTask.distance ?: sharedPreferences.getInt(
            "distance",
            1
        ) * 1609.0F

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
            setInitialTrigger(0)
            addGeofences(geofences)
        }.build()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return NavigationUI.navigateUp(navController, null)
    }
}