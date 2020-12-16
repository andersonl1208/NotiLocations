package com.notilocations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable version of a location.
 * @param locationId The id of the location.
 * @param name The name of the location.
 * @param lat The latitude of the location.
 * @param lng The longitude of the location.
 */
@Parcelize
data class NotiLocation(
    var locationId: Long? = null,
    var name: String? = null,
    var lat: Double = 0.0,
    var lng: Double = 0.0
) : Parcelable {
    /**
     * Returns true if this NotiLocation has an id set.
     * @return Whether or not the location id is null.
     */
    fun hasId(): Boolean {
        return locationId != null
    }
}