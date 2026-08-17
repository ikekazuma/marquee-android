package dev.ikekazuma.marquee.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Destinations live here rather than in the feature modules, so features stay unaware
 * of each other and only report events upwards.
 */
sealed interface AppNavKey : NavKey {
    @Serializable
    data object NowPlaying : AppNavKey

    @Serializable
    data class Detail(val movieId: Int) : AppNavKey
}
