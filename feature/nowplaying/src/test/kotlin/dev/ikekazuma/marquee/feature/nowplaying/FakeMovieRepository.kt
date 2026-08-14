package dev.ikekazuma.marquee.feature.nowplaying

import androidx.paging.PagingData
import dev.ikekazuma.marquee.core.data.MovieRepository
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

internal fun movie(id: Int) =
    Movie(
        id = MovieId(id),
        title = "Movie $id",
        posterUrl = null,
        releaseDate = LocalDate.of(2026, 8, 1),
        voteAverage = 7.5,
    )

internal class FakeMovieRepository(private val movies: List<Movie> = (1..3).map(::movie)) : MovieRepository {
    var pagerCallCount = 0
        private set

    override fun nowPlayingPager(): Flow<PagingData<Movie>> {
        pagerCallCount++
        return flowOf(PagingData.from(movies))
    }
}
