import dev.ikekazuma.marquee.buildlogic.libs
import dev.ikekazuma.marquee.buildlogic.library
import dev.ikekazuma.marquee.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("kotlin-jvm"))

        extensions.configure<KotlinJvmProjectExtension> {
            jvmToolchain(17)
        }

        dependencies {
            add("testImplementation", libs.library("junit"))
            add("testImplementation", libs.library("kotlinx-coroutines-test"))
        }
    }
}
