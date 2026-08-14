package dev.ikekazuma.marquee.core.data

import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import dev.ikekazuma.marquee.core.model.PagedMovies
import dev.ikekazuma.marquee.core.network.MovieRemoteDataSource
import java.time.LocalDate

internal fun movie(id: Int) =
    Movie(
        id = MovieId(id),
        title = "Movie $id",
        posterUrl = "https://image.tmdb.org/t/p/w500/$id.jpg",
        releaseDate = LocalDate.of(2026, 8, 1),
        voteAverage = 7.5,
    )

/** Serves [totalPages] pages of [pageSize] movies, or fails with [failure] when set. */
internal class FakeMovieRemoteDataSource(
    private val totalPages: Int = 3,
    private val pageSize: Int = 20,
    var failure: AppError? = null,
) : MovieRemoteDataSource {
    val requestedPages = mutableListOf<Int>()

    override suspend fun nowPlaying(page: Int): AppResult<PagedMovies> {
        requestedPages += page
        failure?.let { return AppResult.Failure(it) }
        val firstId = (page - 1) * pageSize + 1
        return AppResult.Success(
            PagedMovies(
                page = page,
                totalPages = totalPages,
                items = (firstId until firstId + pageSize).map(::movie),
            ),
        )
    }

    override suspend fun upcoming(page: Int): AppResult<PagedMovies> = nowPlaying(page)

    override suspend fun search(query: String, page: Int): AppResult<PagedMovies> = nowPlaying(page)

    override suspend fun movieDetail(id: MovieId): AppResult<MovieDetail> = AppResult.Failure(AppError.Unknown)
}
