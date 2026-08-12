package dev.ikekazuma.marquee.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension) {
    commonExtension.buildFeatures.compose = true


    dependencies {
        val bom = platform(libs.library("androidx-compose-bom"))
        add("implementation", bom)
        add("androidTestImplementation", bom)

        listOf(
            "androidx-compose-ui",
            "androidx-compose-ui-graphics",
            "androidx-compose-ui-tooling-preview",
            "androidx-compose-material3",
            "androidx-activity-compose",
            "androidx-lifecycle-runtime-compose",
        ).forEach { add("implementation", libs.library(it)) }

        add("debugImplementation", libs.library("androidx-compose-ui-tooling"))
        add("debugImplementation", libs.library("androidx-compose-ui-test-manifest"))
        add("androidTestImplementation", libs.library("androidx-compose-ui-test-junit4"))
    }
}
