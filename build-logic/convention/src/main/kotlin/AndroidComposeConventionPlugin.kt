import com.android.build.api.dsl.CommonExtension
import dev.ikekazuma.marquee.buildlogic.configureAndroidCompose
import dev.ikekazuma.marquee.buildlogic.libs
import dev.ikekazuma.marquee.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("kotlin-compose"))

        configureAndroidCompose(extensions.getByType<CommonExtension>())
    }
}
