package com.hara.kaera.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hara.kaera.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val keyHash = Utility.getKeyHash(this)
        Timber.e("keyhash : $keyHash")
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
    }

}