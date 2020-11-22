package com.notilocations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.notilocations.database.FullLocationTask
import com.notilocations.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        createNotificationChannel()

        val response =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (response != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1)
                .show()
        } else {
            createGeofences()
        }

//        NotiLocationsRepository.getInstance((application)).insertLocation(Location(45, "test once", 0.0, 0.0))
//
//        val temp = NotiLocationsRepository.getInstance(application).getLocation(45)
//        val test2 = NotiLocationsRepository.getInstance(application).getAllLocations()
//        temp.observe(this, Observer<Location> { test3 ->
//            if (test3 != null) {
//                Log.i("MyLogTest", test3.name + test3.lat + test3.lng)
////                NotiLocationsRepository.getInstance(application).deleteLocation(test3)
//            }
//        })
//
//        NotiLocationsRepository.getInstance((application)).updateLocation(Location(45, "test something different", 1.1, 1.1))
//
//
//        test2.observe(this, Observer<List<Location>> { what ->
//            if (what != null) {
//                for (l in what) {
//                    Log.i("MyLogTest", l.name + l.lat + l.lng)
//                }
//            }
//        })
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


    private fun createGeofences() {
        //val go

        return
    }

    private fun createGeofence(fullLocationTask: FullLocationTask): Geofence? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var radius = fullLocationTask.locationTask.distance ?: sharedPreferences.getFloat(
            "distance",
            1.0F
        ) * 1609
        return Geofence.Builder()
            .setRequestId(fullLocationTask.locationTask.id.toString())
            .setCircularRegion(fullLocationTask.location.lat, fullLocationTask.location.lng, radius)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
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