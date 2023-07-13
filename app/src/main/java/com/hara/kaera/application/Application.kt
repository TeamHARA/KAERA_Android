package com.hara.kaera.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.hara.kaera.BuildConfig
import timber.log.Timber

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}