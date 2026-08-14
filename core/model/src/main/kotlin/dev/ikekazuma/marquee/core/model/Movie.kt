package dev.ikekazuma.marquee.core.model

import java.time.LocalDate

@JvmInline
value class MovieId(val value: Int)

data class Movie(
    val id: MovieId,
    val title: String,
    val posterUrl: String?,
    val releaseDate: LocalDate?,
    val voteAverage: Double,
)
