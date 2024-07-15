// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

}
buildscript{
    val hiltVersion = "2.51.1"
    val gsversion = "4.4.2"
    dependencies{
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        classpath ("com.google.gms:google-services:$gsversion")
    }
}