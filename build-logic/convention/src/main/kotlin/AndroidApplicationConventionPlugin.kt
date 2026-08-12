import com.android.build.api.dsl.ApplicationExtension
import dev.ikekazuma.marquee.buildlogic.TARGET_SDK
import dev.ikekazuma.marquee.buildlogic.configureKotlinAndroid
import dev.ikekazuma.marquee.buildlogic.libs
import dev.ikekazuma.marquee.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-application"))

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid()
            defaultConfig.targetSdk = TARGET_SDK
        }
    }
}
