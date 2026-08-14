package dev.ikekazuma.marquee.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.ikekazuma.marquee.R
import dev.ikekazuma.marquee.feature.nowplaying.NowPlayingRoute
import dev.ikekazuma.marquee.navigation.AppNavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarqueeApp() {
    val backStack = rememberNavBackStack(AppNavKey.NowPlaying)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            entryProvider =
                entryProvider {
                    entry<AppNavKey.NowPlaying> {
                        // Detail navigation arrives with M3.
                        NowPlayingRoute(onMovieClick = {})
                    }
                },
        )
    }
}
