package com.example.fcmexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.fcmexample.db.FCMExampleDB
import com.example.fcmexample.db.Notification

class FCMViewModel(private val db: FCMExampleDB): ViewModel() {

    val notifications = db.notificationsDao().getNotificationsObservable()

}