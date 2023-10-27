object Dependency {

    object AndroidX {
        const val CORE = "androidx.core:core-ktx:${Versions.CORE_KTX}"
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
        const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINTLAYOUT}"
        const val LEGACY = "androidx.legacy:legacy-support-v4:${Versions.LEGACY_SUPPORT}"
        const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE_LIVEDATA}"
        const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE_VIEWMODEL}"
        const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.ACTIVITY}"
        const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
        const val VIEWPAGER2 = "androidx.viewpager2:viewpager2:${Versions.VIEWPAGER2 }"
        const val DATASTORE = "androidx.datastore:datastore:${Versions.DATASTORE}"
        const val SPLASH_SCREEN = "androidx.core:core-splashscreen:${Versions.SPLASH_SCREEN}"
    }

    object Android {
        const val MATERIAL = "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"
    }

    object Google{
        const val GOOGLE_SERVICE = "com.google.gms:google-services:${Versions.GOOGLE_SERVICE}"
    }

    object Firebase {
        const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
        const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
        const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
        const val FIREBASE_MESSAGING = "com.google.firebase:firebase-messaging-ktx:${Versions.FIREBASE_MEESAGING}"
    }

    object ThirdParty {
        const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
        const val DOT_INDICATOR = "com.tbuonomo:dotsindicator:${Versions.DOTSINDICATOR}"
        const val LOTTIE = "com.airbnb.android:lottie:${Versions.LOTTIE}"
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
    }

    object AndroidTest {
        const val JUNIT = "androidx.test.ext:${Versions.ANDROIDX_JUNIT}"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
    }

    object Kotlin{
        const val KOTLIN_SERIALIZATION_PLUGIN = "org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}"
        const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATIOM_JSON}"
        const val COIL = "io.coil-kt:coil:${Versions.COIL}"
    }

    object Hilt {
        const val HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
        const val DAGGER_HILT = "com.google.dagger:hilt-android:${Versions.HILT_ANDROID}"
        const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT_COMPILER}"
    }

    object Okhttp3{
        const val OKHTTP3_BOM = "com.squareup.okhttp3:okhttp-bom:${Versions.OKHTTP_BOM}"
        const val OKHTTP3 = "com.squareup.okhttp3:okhttp"
        const val OKHTTP3_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor"
    }

    object Retrofit2{
        const val SQUAREUP_RETROFIT2 = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT2}"
        const val RETROFIR2_SERIALIZATIOM = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.RETROFIT2_SERIALIZATION_CONVERTER}"
    }

    object KaKaoSDK{
        const val KAKAO_MODULE = "com.kakao.sdk:v2-all:${Versions.KAKAO_SDK}"
        const val KAKAO_LOGIN = "com.kakao.sdk:v2-user:${Versions.KAKAO_SDK}"
        const val KAKAO_USER = "com.kakao.sdk:v2-talk:${Versions.KAKAO_SDK}"
    }

    object SecertsGradle{
        const val SECERETS_GRADLE_CLASSPATH ="com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:{${Versions.SERCRET_GRADLE}}"
    }

}

