package com.example.demoapp

import android.app.Application
import com.example.demoapp.menu.di.Injector
import timber.log.Timber

class DemoApp:Application(){
    val injector by lazy { Injector(this) }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}