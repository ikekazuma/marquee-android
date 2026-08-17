package dev.ikekazuma.marquee.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.ikekazuma.marquee.core.model.MovieId
import dev.ikekazuma.marquee.feature.detail.DetailRoute
import dev.ikekazuma.marquee.feature.nowplaying.NowPlayingRoute
import dev.ikekazuma.marquee.navigation.AppNavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarqueeApp() {
    val backStack = rememberNavBackStack(AppNavKey.NowPlaying)

    val canNavigateBack = backStack.size > 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = { backStack.removeLastOrNull() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    }
                },
            )
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
                        NowPlayingRoute(onMovieClick = { movieId -> backStack.add(AppNavKey.Detail(movieId.value)) })
                    }
                    entry<AppNavKey.Detail> { key ->
                        DetailRoute(movieId = MovieId(key.movieId))
                    }
                },
        )
    }
}
