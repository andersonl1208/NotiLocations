package com.notilocations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.notilocations.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

/**
 * Main activity for the app. Created when the app starts up.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Sets up the main activity for the app, sets up geofences, and sets dark mode if necessary.
     * @param savedInstanceState The previously saved state of the activity to load if it exists.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        setupGeofences()
        handleDarkMode()

        val response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (response != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1)
                .show()
        }
    }

    /**
     * Handles geofence related setup.
     */
    private fun setupGeofences() {
        val addGeofencesRequest =
            PeriodicWorkRequestBuilder<AddGeofencesWorker>(15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            "AddGeofencesRequestWork",
            ExistingPeriodicWorkPolicy.KEEP,
            addGeofencesRequest
        )

        HandleGeofences.getInstance(this).create()
    }

    /**
     * If the dark theme is enabled, sets the app to use it.
     */
    private fun handleDarkMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (sharedPreferences?.getBoolean("dark_theme", false) == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * Cleans up the repository when the main activity is paused.
     */
    override fun onPause() {
        ViewModelProvider(this).get(NotiLocationsViewModel::class.java).cleanUp()
        super.onPause()
    }

    /**
     * Creates a notification channel for location notifications on builds above Android O.
     */
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
}