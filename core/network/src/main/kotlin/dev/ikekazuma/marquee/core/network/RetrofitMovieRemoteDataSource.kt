package dev.ikekazuma.marquee.core.network

import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import dev.ikekazuma.marquee.core.model.PagedMovies
import dev.ikekazuma.marquee.core.network.dto.toDomain
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class RetrofitMovieRemoteDataSource
    @Inject
    constructor(private val api: TmdbApi) :
    MovieRemoteDataSource {
        override suspend fun nowPlaying(page: Int): AppResult<PagedMovies> =
            runCatchingApi { api.nowPlaying(page).toDomain() }

        override suspend fun upcoming(page: Int): AppResult<PagedMovies> =
            runCatchingApi { api.upcoming(page).toDomain() }

        override suspend fun search(query: String, page: Int): AppResult<PagedMovies> =
            runCatchingApi {
                api.search(query, page).toDomain()
            }

        override suspend fun movieDetail(id: MovieId): AppResult<MovieDetail> =
            runCatchingApi {
                api.movieDetail(id.value).toDomain()
            }
    }

// The single place where exceptions become AppError; nothing above this layer sees a throwable.
private inline fun <T> runCatchingApi(block: () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (e: HttpException) {
        AppResult.Failure(AppError.Http(e.code()))
    } catch (_: IOException) {
        AppResult.Failure(AppError.Network)
    } catch (_: Exception) {
        AppResult.Failure(AppError.Unknown)
    }
