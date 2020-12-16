package com.notilocations

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Worker class that adds geofences when doWork is called.
 */
class AddGeofencesWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * Creates geofences and returns Result.success()
     */
    override fun doWork(): Result {
        HandleGeofences.getInstance(context).create()

        return Result.success()
    }
}