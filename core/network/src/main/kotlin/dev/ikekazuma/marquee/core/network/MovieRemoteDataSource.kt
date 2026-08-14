package dev.ikekazuma.marquee.core.network

import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import dev.ikekazuma.marquee.core.model.PagedMovies

interface MovieRemoteDataSource {
    suspend fun nowPlaying(page: Int): AppResult<PagedMovies>

    suspend fun upcoming(page: Int): AppResult<PagedMovies>

    suspend fun search(query: String, page: Int): AppResult<PagedMovies>

    suspend fun movieDetail(id: MovieId): AppResult<MovieDetail>
}
