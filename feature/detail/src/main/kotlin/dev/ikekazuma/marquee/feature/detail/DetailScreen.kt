package dev.ikekazuma.marquee.feature.detail

import android.content.Intent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ikekazuma.marquee.core.designsystem.component.ErrorContent
import dev.ikekazuma.marquee.core.designsystem.component.LoadingContent
import dev.ikekazuma.marquee.core.designsystem.component.MoviePoster
import dev.ikekazuma.marquee.core.designsystem.component.RatingBadge
import dev.ikekazuma.marquee.core.model.CastMember
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import java.time.format.DateTimeFormatter

private val ReleaseDateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
private const val YOUTUBE_WATCH_URL = "https://www.youtube.com/watch?v="
private const val CAST_COLUMN_WIDTH_DP = 96

@Composable
fun DetailRoute(
    movieId: MovieId,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel =
        hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
            creationCallback = { factory -> factory.create(movieId.value) },
        ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScreen(uiState = uiState, onRetry = viewModel::retry, modifier = modifier)
}

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        DetailUiState.Loading -> LoadingContent(modifier)
        is DetailUiState.Error -> ErrorContent(error = uiState.error, onRetry = onRetry, modifier = modifier)
        is DetailUiState.Success -> DetailContent(detail = uiState.detail, modifier = modifier)
    }
}

@Composable
private fun DetailContent(detail: MovieDetail, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MovieHeader(detail)

        detail.trailerYouTubeKey?.let { key ->
            Button(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, (YOUTUBE_WATCH_URL + key).toUri()))
                },
            ) {
                Text(text = stringResource(R.string.detail_watch_trailer))
            }
        }

        Text(text = stringResource(R.string.detail_overview), style = MaterialTheme.typography.titleMedium)
        Text(
            text = detail.overview.ifBlank { stringResource(R.string.detail_overview_missing) },
            style = MaterialTheme.typography.bodyMedium,
        )

        if (detail.cast.isNotEmpty()) {
            Text(text = stringResource(R.string.detail_cast), style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                detail.cast.take(MAX_CAST).forEach { member -> CastColumn(member) }
            }
        }
    }
}

@Composable
private fun MovieHeader(detail: MovieDetail) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        MoviePoster(
            url = detail.posterUrl,
            contentDescription = null,
            modifier = Modifier.width(140.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = detail.title, style = MaterialTheme.typography.titleLarge)
            if (detail.originalTitle.isNotBlank() && detail.originalTitle != detail.title) {
                Text(
                    text = detail.originalTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text =
                    detail.releaseDate?.format(ReleaseDateFormat)
                        ?: stringResource(R.string.detail_release_date_unknown),
                style = MaterialTheme.typography.bodyMedium,
            )
            detail.runtimeMinutes?.let { runtime ->
                Text(
                    text = stringResource(R.string.detail_runtime, runtime),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (detail.genres.isNotEmpty()) {
                Text(
                    text = detail.genres.joinToString(" / "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (detail.voteAverage > 0) {
                RatingBadge(voteAverage = detail.voteAverage)
            }
        }
    }
}

@Composable
private fun CastColumn(member: CastMember) {
    Column(
        modifier = Modifier.width(CAST_COLUMN_WIDTH_DP.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        MoviePoster(url = member.profileUrl, contentDescription = null)
        Text(
            text = member.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (member.character.isNotBlank()) {
            Text(
                text = member.character,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private const val MAX_CAST = 10
