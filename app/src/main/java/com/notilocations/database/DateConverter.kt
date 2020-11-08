package com.notilocations.database

import androidx.room.TypeConverter
import java.util.*

/**
 * Converts a date object to a timestamp stored as a long.
 */
class DateConverter {
    /**
     * Converts a date object to a timestamp.
     * @param date The date object to convert.
     * @return A timestamp stored as a long.
     */
    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    /**
     * Converts a timestamp to a date object.
     * @param timestamp The timestamp to convert.
     * @return A date object with the timestamp.
     */
    @TypeConverter
    fun longToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}