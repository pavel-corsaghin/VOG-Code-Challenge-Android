package com.example.fcmexample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fcmexample.R

@Database(
    entities = [
        Notification::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FCMExampleDB: RoomDatabase() {
    abstract fun notificationsDao(): NotificationsDao

    companion object {
        @Volatile
        private var INSTANCE: FCMExampleDB? = null

        fun getDatabase(context: Context): FCMExampleDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FCMExampleDB::class.java,
                    "${context.getString(R.string.app_name)}_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}