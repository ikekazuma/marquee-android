package dev.ikekazuma.marquee.core.network

internal object TmdbImage {
    private const val BASE_URL = "https://image.tmdb.org/t/p"

    fun poster(path: String?): String? = path?.let { "$BASE_URL/w500$it" }

    fun backdrop(path: String?): String? = path?.let { "$BASE_URL/w780$it" }

    fun profile(path: String?): String? = path?.let { "$BASE_URL/w185$it" }
}
