// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(Dependency.Kotlin.KOTLIN_SERIALIZATION_PLUGIN)
        classpath(Dependency.Hilt.HILT_PLUGIN)
        classpath(Dependency.Google.GOOGLE_SERVICE)
    }
}


plugins {
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.7" apply false
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}