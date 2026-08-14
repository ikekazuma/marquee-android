import java.util.Properties

plugins {
    id("marquee.android.library")
    id("marquee.hilt")
    alias(libs.plugins.kotlin.serialization)
}

// CI passes the token through the environment; locally it lives in local.properties (git-ignored).
// An empty token still builds: only calls against the real API fail.
val tmdbAccessToken: String =
    providers.environmentVariable("TMDB_ACCESS_TOKEN").orNull
        ?: rootProject
            .file("local.properties")
            .takeIf { it.exists() }
            ?.let { file -> Properties().apply { file.inputStream().use(::load) }.getProperty("TMDB_ACCESS_TOKEN") }
        ?: ""

android {
    namespace = "dev.ikekazuma.marquee.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "TMDB_ACCESS_TOKEN", "\"$tmdbAccessToken\"")
    }
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.okhttp.mockwebserver)
}
