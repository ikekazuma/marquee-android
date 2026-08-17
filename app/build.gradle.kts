plugins {
    id("marquee.android.application")
    alias(libs.plugins.kotlin.serialization)
    id("marquee.android.compose")
    id("marquee.hilt")
}

android {
    namespace = "dev.ikekazuma.marquee"

    defaultConfig {
        applicationId = "dev.ikekazuma.marquee"
        versionCode = 1
        versionName = "0.1.0"
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.feature.detail)
    implementation(projects.feature.nowplaying)

    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
