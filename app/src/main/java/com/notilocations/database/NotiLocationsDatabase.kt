package com.notilocations.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * The NotiLocations database class.
 */
@Database(entities = [Location::class, LocationTask::class, Task::class], version = 3)
@TypeConverters(DateConverter::class)
abstract class NotiLocationsDatabase : RoomDatabase() {

    /**
     * The location dao.
     * @return The location dao.
     */
    abstract fun locationDao(): LocationDao

    /**
     * The task dao.
     * @return The task dao.
     */
    abstract fun taskDao(): TaskDao

    /**
     * The location task dao.
     * @return The location task dao.
     */
    abstract fun locationTaskDao(): LocationTaskDao

    /**
     * Companion object that returns an instance of the NotiLocations database class, allowing it to be used as a singleton.
     * @property instance Holds an instance of the NotiLocations database after it is initialized by the first call to get the instance.
     */
    companion object {
        private var instance: NotiLocationsDatabase? = null

        /**
         * Gets an instance of the NotiLocations database.
         * @param context The context the database is being created in.
         * @return An instance of the NotiLocations database.
         */
        fun getInstance(context: Context): NotiLocationsDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    NotiLocationsDatabase::class.java,
                    "notilocations_db"
                )
                    //.fallbackToDestructiveMigration() // Destroys the database if needed
                    .build()
            }

            return instance as NotiLocationsDatabase
        }
    }
}