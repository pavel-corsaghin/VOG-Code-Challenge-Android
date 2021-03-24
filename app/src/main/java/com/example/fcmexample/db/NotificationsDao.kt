package com.example.fcmexample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationsDao {

    @Query("SELECT * FROM notifications")
    fun getNotificationsObservable(): LiveData<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(notification: Notification): Long

}