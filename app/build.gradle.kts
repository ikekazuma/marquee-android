plugins {
    id("marquee.android.application")
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
    implementation(libs.timber)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
