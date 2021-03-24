package com.example.fcmexample.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fcmexample.BaseApplication
import com.example.fcmexample.FCMViewModel
import com.example.fcmexample.SendNotificationVM
import com.example.fcmexample.db.FCMExampleDB

class ViewModelProviderFactory: ViewModelProvider.Factory  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FCMViewModel::class.java)) {
            return FCMViewModel(FCMExampleDB.getDatabase(BaseApplication.instance)) as T
        }
        if (modelClass.isAssignableFrom(SendNotificationVM::class.java)){
            return SendNotificationVM() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}