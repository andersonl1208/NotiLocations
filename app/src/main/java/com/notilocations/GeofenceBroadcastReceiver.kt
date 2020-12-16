package com.notilocations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Broadcast receiver for geofence broadcasts.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    /**
     * Called when a broadcast is received. Puts the received intent into the work queue of the geofence job intent service.
     * @param context The context the broadcast receiver was called from.
     * @param intent The intent that was sent by the broadcast to be handled.
     */
    override fun onReceive(context: Context, intent: Intent) {
        GeofenceJobIntentService.enqueueWork(context, intent)
    }
}