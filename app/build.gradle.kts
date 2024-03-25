import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.android.gms.oss-licenses-plugin")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())


val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.hara.kaera"
    compileSdk = 33

    signingConfigs {
        create("realse") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }


    defaultConfig {
        applicationId = "com.hara.kaera"
        minSdk = 28
        targetSdk = 33
        versionCode = 2
        versionName = "1.1"

        buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
        buildConfigField("String", "BEARER_TOKEN", properties.getProperty("BEARER_TOKEN"))
        buildConfigField("String", "NATIVE_APP_KEY", properties.getProperty("NATIVE_APP_KEY"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("realse")
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
    implementation(Dependency.AndroidX.DATASTORE)
    implementation(Dependency.AndroidX.SPLASH_SCREEN)

    // ViewModel delegate: by viewModels() 이용해 viewModel 생성
    implementation(Dependency.AndroidX.ACTIVITY)
    implementation(Dependency.AndroidX.FRAGMENT)

    implementation(Dependency.Android.MATERIAL)
    implementation("com.google.android.material:material:1.9.0")

    testImplementation(Dependency.Test.JUNIT)
    androidTestImplementation(Dependency.AndroidTest.JUNIT)
    androidTestImplementation(Dependency.AndroidTest.KTX_JUNIT)
    androidTestImplementation(Dependency.AndroidTest.ESPRESSO)

    implementation(platform(Dependency.Firebase.FIREBASE_BOM))
    implementation(Dependency.Firebase.FIREBASE_ANALYTICS)
    implementation(Dependency.Firebase.FIREBASE_CRASHLYTICS)
    implementation(Dependency.Firebase.FIREBASE_MESSAGING)
    implementation(Dependency.Firebase.FIREBASE_RMOTECONFIG)

    //Timber
    implementation(Dependency.ThirdParty.TIMBER)
    //viewpager2 dot indicator
    implementation(Dependency.ThirdParty.DOT_INDICATOR)
    //Lottie
    implementation(Dependency.ThirdParty.LOTTIE)

    implementation(Dependency.Kotlin.COIL)
    implementation(Dependency.Kotlin.KOTLIN_SERIALIZATION)

    implementation(Dependency.Retrofit2.SQUAREUP_RETROFIT2)
    implementation(Dependency.Retrofit2.RETROFIT2_SERIALIZATION)

    implementation(platform(Dependency.Okhttp3.OKHTTP3_BOM))
    implementation(Dependency.Okhttp3.OKHTTP3)
    implementation(Dependency.Okhttp3.OKHTTP3_LOGGING_INTERCEPTOR)

    implementation(Dependency.Hilt.DAGGER_HILT)
    kapt(Dependency.Hilt.HILT_COMPILER)

    implementation(Dependency.KaKaoSDK.KAKAO_MODULE)
    implementation(Dependency.KaKaoSDK.KAKAO_LOGIN)
    implementation(Dependency.KaKaoSDK.KAKAO_USER)

    //oss-licenses
    implementation(Dependency.Android.OSS_LICENSES_LIBRARY)
}