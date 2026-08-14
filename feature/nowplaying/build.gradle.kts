plugins {
    id("marquee.android.feature")
}

android {
    namespace = "dev.ikekazuma.marquee.feature.nowplaying"
}

dependencies {
    implementation(projects.core.data)
    implementation(libs.androidx.paging.compose)

    testImplementation(libs.androidx.paging.testing)
}
