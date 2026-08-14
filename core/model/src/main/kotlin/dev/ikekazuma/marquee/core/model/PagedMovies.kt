package dev.ikekazuma.marquee.core.model

data class PagedMovies(
    val page: Int,
    val totalPages: Int,
    val items: List<Movie>,
)
