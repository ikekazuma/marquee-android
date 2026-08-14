plugins {
    id("marquee.android.library")
    id("marquee.android.compose")
}

android {
    namespace = "dev.ikekazuma.marquee.core.designsystem"
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
