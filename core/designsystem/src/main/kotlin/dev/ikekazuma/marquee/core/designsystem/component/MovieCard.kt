package dev.ikekazuma.marquee.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ikekazuma.marquee.core.designsystem.R
import dev.ikekazuma.marquee.core.designsystem.theme.MarqueeTheme
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieId
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val ReleaseDateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(4.dp),
    ) {
        Box {
            MoviePoster(
                url = movie.posterUrl,
                contentDescription = stringResource(R.string.ds_poster_of, movie.title),
            )
            if (movie.voteAverage > 0) {
                RatingBadge(
                    voteAverage = movie.voteAverage,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp),
                )
            }
        }
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(
            text = movie.releaseDate?.format(ReleaseDateFormat) ?: stringResource(R.string.ds_release_date_unknown),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun MovieCardPreview() {
    MarqueeTheme(dynamicColor = false) {
        MovieCard(
            movie =
                Movie(
                    id = MovieId(1),
                    title = "とても長い邦題がついた映画のタイトル",
                    posterUrl = null,
                    releaseDate = LocalDate.of(2026, 8, 14),
                    voteAverage = 7.8,
                ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
