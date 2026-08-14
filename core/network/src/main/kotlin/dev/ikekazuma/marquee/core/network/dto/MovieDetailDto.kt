package dev.ikekazuma.marquee.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieDetailDto(
    val id: Int,
    val title: String,
    @SerialName("original_title") val originalTitle: String = "",
    val overview: String = "",
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    val runtime: Int? = null,
    val genres: List<GenreDto> = emptyList(),
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    val credits: CreditsDto? = null,
    val videos: VideosDto? = null,
)

@Serializable
internal data class GenreDto(
    val id: Int,
    val name: String,
)

@Serializable
internal data class CreditsDto(val cast: List<CastMemberDto> = emptyList())

@Serializable
internal data class CastMemberDto(
    val name: String,
    val character: String = "",
    @SerialName("profile_path") val profilePath: String? = null,
    val order: Int = 0,
)

@Serializable
internal data class VideosDto(val results: List<VideoDto> = emptyList())

@Serializable
internal data class VideoDto(
    val key: String,
    val site: String,
    val type: String,
    val official: Boolean = false,
)
