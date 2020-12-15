package com.notilocations

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AddGeofencesWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        HandleGeofences.getInstance(context).create()

        return Result.success()
    }
}