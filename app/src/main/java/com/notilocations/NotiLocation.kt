package com.notilocations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotiLocation(
    var locationId: Long? = null,
    var name: String? = null,
    var lat: Double = 0.0,
    var lng: Double = 0.0
) : Parcelable {
    fun hasId(): Boolean {
        return locationId != null
    }
}