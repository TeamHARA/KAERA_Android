// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(Dependency.Kotlin.KOTLIN_SERIALIZATION_PLUGIN)
        classpath(Dependency.Hilt.HILT_PLUGIN)
        classpath(Dependency.Google.GOOGLE_SERVICE)
        classpath(Dependency.SecretsGradle.SECRETS_GRADLE_CLASSPATH)
        classpath(Dependency.Android.OSS_LICENSES_PLUGIN)
    }
}


plugins {
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.7" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}