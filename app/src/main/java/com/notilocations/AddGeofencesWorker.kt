package com.notilocations

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class AddGeofencesWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        HandleGeofences.getInstance(context).create()

//        val notificationBuilder =
//            NotificationCompat.Builder(context, "0")
//                .setSmallIcon(R.drawable.ic_stat_temp_notification)
//                .setContentTitle("Geofence worker called")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(context)) {
//            // notificationId is a unique int for each notification that you must define
//            notify(1584818418, notificationBuilder.build())
//        }

        return Result.success()
    }
}