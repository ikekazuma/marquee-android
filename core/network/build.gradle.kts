plugins {
    id("marquee.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.ikekazuma.marquee.core.network"
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.core)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.okhttp.mockwebserver)
}
