package dev.ikekazuma.marquee.core.network

import dev.ikekazuma.marquee.core.network.dto.MovieDetailDto
import dev.ikekazuma.marquee.core.network.dto.PagedMovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface TmdbApi {
    @GET("movie/now_playing")
    suspend fun nowPlaying(
        @Query("page") page: Int,
        @Query("region") region: String = DEFAULT_REGION,
        @Query("language") language: String = DEFAULT_LANGUAGE,
    ): PagedMovieListDto

    @GET("movie/upcoming")
    suspend fun upcoming(
        @Query("page") page: Int,
        @Query("region") region: String = DEFAULT_REGION,
        @Query("language") language: String = DEFAULT_LANGUAGE,
    ): PagedMovieListDto

    @GET("search/movie")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("region") region: String = DEFAULT_REGION,
        @Query("language") language: String = DEFAULT_LANGUAGE,
    ): PagedMovieListDto

    @GET("movie/{id}")
    suspend fun movieDetail(
        @Path("id") id: Int,
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("append_to_response") append: String = DETAIL_APPEND,
    ): MovieDetailDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val DEFAULT_REGION = "JP"
        private const val DEFAULT_LANGUAGE = "ja-JP"
        private const val DETAIL_APPEND = "credits,videos"
    }
}
