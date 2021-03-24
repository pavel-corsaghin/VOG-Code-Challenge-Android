package com.example.fcmexample.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notifications")
@Parcelize
data class Notification(
    @PrimaryKey
    val timeStamp: Long,
    val content: String
) : Parcelable