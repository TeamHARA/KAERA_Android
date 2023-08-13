import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.hara.kaera"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.hara.kaera"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "BASE_URL",
            gradleLocalProperties(rootDir).getProperty("BASE_URL")
        )
        buildConfigField(
            "String",
            "BEARER_TOKEN",
            gradleLocalProperties(rootDir).getProperty("BEARER_TOKEN")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(Dependency.AndroidX.CORE)
    implementation(Dependency.AndroidX.APPCOMPAT)
    implementation(Dependency.AndroidX.CONSTRAINTLAYOUT)
    implementation(Dependency.AndroidX.LEGACY)
    implementation(Dependency.AndroidX.LIVEDATA)
    implementation(Dependency.AndroidX.VIEWMODEL)
    implementation(Dependency.AndroidX.VIEWPAGER2)

    // ViewModel delegate: by viewModels() 이용해 viewModel 생성
    implementation(Dependency.AndroidX.ACTIVITY)
    implementation(Dependency.AndroidX.FRAGMENT)

    implementation(Dependency.Android.MATERIAL)

    testImplementation(Dependency.Test.JUNIT)
    androidTestImplementation(Dependency.AndroidTest.JUNIT)
    androidTestImplementation(Dependency.AndroidTest.ESPRESSO)

    implementation(platform(Dependency.Firebase.FIREBASE_BOM))
    implementation(Dependency.Firebase.FIREBASE_ANALYTICS)
    implementation(Dependency.Firebase.FIREBASE_CRASHLYTICS)

    //Timber
    implementation(Dependency.ThirdParty.TIMBER)
    // home viewpager2 dot indicator
    implementation(Dependency.ThirdParty.DOT_INDICATOR)

    implementation(Dependency.Kotlin.COIL)
    implementation(Dependency.Kotlin.KOTLIN_SERIALIZATION)

    implementation(Dependency.Retrofit2.SQUAREUP_RETROFIT2)
    implementation(Dependency.Retrofit2.RETROFIR2_SERIALIZATIOM)

    implementation(platform(Dependency.Okhttp3.OKHTTP3_BOM))
    implementation(Dependency.Okhttp3.OKHTTP3)
    implementation(Dependency.Okhttp3.OKHTTP3_LOGGING_INTERCEPTOR)

    implementation(Dependency.Hilt.DAGGER_HILT)
    kapt(Dependency.Hilt.HILT_COMPILER)
}