package dev.ikekazuma.marquee.core.model

import java.time.LocalDate

data class MovieDetail(
    val id: MovieId,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: LocalDate?,
    val runtimeMinutes: Int?,
    val genres: List<String>,
    val voteAverage: Double,
    val cast: List<CastMember>,
    val trailerYouTubeKey: String?,
)

data class CastMember(
    val name: String,
    val character: String,
    val profileUrl: String?,
)
