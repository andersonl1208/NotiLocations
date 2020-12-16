package com.notilocations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable version of a task.
 * @param taskId The id of the task.
 * @param title The title of the task.
 * @param description A description of the task.
 */
@Parcelize
data class NotiTask(
    var taskId: Long? = null,
    var title: String = "",
    var description: String? = null
) : Parcelable {
    /**
     * Returns true if this NotiTask has an id set.
     * @return Whether or not the task id is null.
     */
    fun hasId(): Boolean {
        return taskId != null
    }
}