package dev.ikekazuma.marquee.core.network.dto

import dev.ikekazuma.marquee.core.model.CastMember
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import dev.ikekazuma.marquee.core.model.PagedMovies
import dev.ikekazuma.marquee.core.network.TmdbImage
import java.time.LocalDate
import java.time.format.DateTimeParseException

internal fun PagedMovieListDto.toDomain(): PagedMovies =
    PagedMovies(
        page = page,
        totalPages = totalPages,
        items = results.map { it.toDomain() },
    )

internal fun MovieDto.toDomain(): Movie =
    Movie(
        id = MovieId(id),
        title = title.ifBlank { originalTitle.orEmpty() },
        posterUrl = TmdbImage.poster(posterPath),
        releaseDate = releaseDate.toLocalDateOrNull(),
        voteAverage = voteAverage,
    )

internal fun MovieDetailDto.toDomain(): MovieDetail =
    MovieDetail(
        id = MovieId(id),
        title = title.ifBlank { originalTitle },
        originalTitle = originalTitle,
        overview = overview,
        posterUrl = TmdbImage.poster(posterPath),
        backdropUrl = TmdbImage.backdrop(backdropPath),
        releaseDate = releaseDate.toLocalDateOrNull(),
        runtimeMinutes = runtime?.takeIf { it > 0 },
        genres = genres.map { it.name },
        voteAverage = voteAverage,
        cast =
            credits
                ?.cast
                .orEmpty()
                .sortedBy { it.order }
                .map { it.toDomain() },
        trailerYouTubeKey = videos?.results.orEmpty().pickTrailerKey(),
    )

private fun CastMemberDto.toDomain(): CastMember =
    CastMember(
        name = name,
        character = character,
        profileUrl = TmdbImage.profile(profilePath),
    )

// Official trailers first, then any trailer, then a teaser.
private fun List<VideoDto>.pickTrailerKey(): String? {
    val youTube = filter { it.site == "YouTube" }
    return youTube.firstOrNull { it.type == "Trailer" && it.official }?.key
        ?: youTube.firstOrNull { it.type == "Trailer" }?.key
        ?: youTube.firstOrNull { it.type == "Teaser" }?.key
}

private fun String?.toLocalDateOrNull(): LocalDate? =
    if (this.isNullOrBlank()) {
        null
    } else {
        try {
            LocalDate.parse(this)
        } catch (_: DateTimeParseException) {
            null
        }
    }
