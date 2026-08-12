import dev.ikekazuma.marquee.buildlogic.libs
import dev.ikekazuma.marquee.buildlogic.library
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("marquee.android.library")
        pluginManager.apply("marquee.android.compose")
        pluginManager.apply("marquee.hilt")

        dependencies {
            add("implementation", libs.library("androidx-hilt-navigation-compose"))
            add("implementation", libs.library("androidx-lifecycle-viewmodel-compose"))
            add("testImplementation", libs.library("turbine"))
        }
    }
}
