package com.example.fcmexample

import android.app.Application
import timber.log.Timber

class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        instance = this
    }
}