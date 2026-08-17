plugins {
    id("marquee.android.feature")
}

android {
    namespace = "dev.ikekazuma.marquee.feature.detail"
}

dependencies {
    implementation(projects.core.data)
}
