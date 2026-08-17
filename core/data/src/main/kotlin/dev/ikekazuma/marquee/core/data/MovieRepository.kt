package dev.ikekazuma.marquee.core.data

import androidx.paging.PagingData
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun nowPlayingPager(): Flow<PagingData<Movie>>

    suspend fun movieDetail(id: MovieId): AppResult<MovieDetail>
}
