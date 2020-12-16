package com.notilocations

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import com.notilocations.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

/**
 * Main activity for the app. Created when the app starts up.
 */
class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    /**
     * Sets up the main activity for the app, sets up geofences, and sets dark mode if necessary.
     * @param savedInstanceState The previously saved state of the activity to load if it exists.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)


        if(checkLocationPermission()){
            Snackbar.make(findViewById(android.R.id.content), "Permission already granted.", Snackbar.LENGTH_LONG).show()
            setupGeofences()
        }
        else{
            Snackbar.make(findViewById(android.R.id.content), "Please request permission.", Snackbar.LENGTH_LONG).show()
            requestLocationPermission()
        }
        //setupGeofences()
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

    /**
     * Checks if the user has permission for fine location
     */
    private fun checkLocationPermission(): Boolean {
            val result = ContextCompat.checkSelfPermission(
                applicationContext,
                ACCESS_FINE_LOCATION
            )
            return result == PackageManager.PERMISSION_GRANTED
        }

    /**
     * Requests the user for fine permissions()
     */
        private fun requestLocationPermission() {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                200
            )
        }

    /**
     * Given the result of the request, it will grant permission, or explain to the user that the app needs permissions
     * @param requestCode the request code of the permission request
     * @param permissions The permissions that were tried
     * @param grantResults
     *
     */
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            when (requestCode) {
                200 -> if (grantResults.isNotEmpty()) {
                    val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    //if the user has permission display a snackbar telling them they granted access
                    if (locationAccepted) Snackbar.make(
                        findViewById(android.R.id.content),
                        "Permission Granted, Now you can access location data",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    //User does not have permission
                    else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Permission Denied, You cannot access location data",
                            Snackbar.LENGTH_LONG
                        ).show()
                        //if the user is above api level 23 display an alert dialog explaining That we need location services for the app to work
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to Location Services inorder to user the app",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(
                                                arrayOf(ACCESS_FINE_LOCATION),
                                                200
                                            )
                                        }
                                    })
                                return
                            }
                        }
                    }
                }
            }
        }

    /**
     * Function for the alert that pops up
     * @param message the message that is displayed on the alert tab
     * @param okListener An onclick listener for the Cancel and OK
     */
    private fun showMessageOKCancel(
            message: String,
            okListener: DialogInterface.OnClickListener
        ) {
            AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
}



