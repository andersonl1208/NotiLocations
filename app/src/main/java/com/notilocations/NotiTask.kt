package com.notilocations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotiTask(
    var taskId: Long? = null,
    var title: String = "",
    var description: String? = null
) : Parcelable {
    fun hasId(): Boolean {
        return taskId != null
    }
}