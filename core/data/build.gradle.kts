plugins {
    id("marquee.android.library")
    id("marquee.hilt")
}

android {
    namespace = "dev.ikekazuma.marquee.core.data"
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)
    api(libs.androidx.paging.runtime)

    implementation(projects.core.network)

    testImplementation(libs.androidx.paging.testing)
}
