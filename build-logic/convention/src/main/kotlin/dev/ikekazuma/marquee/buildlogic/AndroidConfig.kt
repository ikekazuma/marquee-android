package dev.ikekazuma.marquee.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

const val COMPILE_SDK = 37
const val COMPILE_SDK_MINOR = 1
const val MIN_SDK = 36
const val TARGET_SDK = 37

// AGP 9 has built-in Kotlin, so kotlin-android is not applied and
// the Kotlin jvmTarget is derived from compileOptions.targetCompatibility.
internal fun CommonExtension.configureKotlinAndroid() {
    compileSdk = COMPILE_SDK
    compileSdkMinor = COMPILE_SDK_MINOR
    defaultConfig.minSdk = MIN_SDK
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
