package dev.ikekazuma.marquee.feature.nowplaying

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppErrorException
import dev.ikekazuma.marquee.core.designsystem.component.ErrorContent
import dev.ikekazuma.marquee.core.designsystem.component.LoadingContent
import dev.ikekazuma.marquee.core.designsystem.component.MovieCard
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieId

private const val GRID_COLUMNS = 2

@Composable
fun NowPlayingRoute(
    onMovieClick: (MovieId) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NowPlayingViewModel = hiltViewModel(),
) {
    NowPlayingScreen(
        movies = viewModel.movies.collectAsLazyPagingItems(),
        onMovieClick = onMovieClick,
        modifier = modifier,
    )
}

@Composable
fun NowPlayingScreen(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (MovieId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = movies.loadState.refresh
    when {
        refreshState is LoadState.Loading -> LoadingContent(modifier)

        refreshState is LoadState.Error ->
            ErrorContent(
                error = refreshState.error.toAppError(),
                onRetry = movies::retry,
                modifier = modifier,
            )

        movies.itemCount == 0 ->
            Box(modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.nowplaying_empty),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        else -> MovieGrid(movies = movies, onMovieClick = onMovieClick, modifier = modifier)
    }
}

@Composable
private fun MovieGrid(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (MovieId) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        items(count = movies.itemCount, key = { index -> movies.peek(index)?.id?.value ?: index }) { index ->
            movies[index]?.let { movie ->
                MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
            }
        }

        when (val appendState = movies.loadState.append) {
            is LoadState.Loading ->
                item(span = { GridItemSpan(GRID_COLUMNS) }) {
                    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

            is LoadState.Error ->
                item(span = { GridItemSpan(GRID_COLUMNS) }) {
                    AppendError(error = appendState.error.toAppError(), onRetry = movies::retry)
                }

            else -> Unit
        }
    }
}

@Composable
private fun AppendError(error: AppError, onRetry: () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
        if (error == AppError.Unauthorized) {
            Text(
                text = stringResource(R.string.nowplaying_append_failed),
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.nowplaying_append_retry))
            }
        }
    }
}

// Paging can only carry Throwables, so the data layer wraps AppError on the way out.
private fun Throwable.toAppError(): AppError = (this as? AppErrorException)?.appError ?: AppError.Unknown
