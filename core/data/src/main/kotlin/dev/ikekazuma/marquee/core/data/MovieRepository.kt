package dev.ikekazuma.marquee.core.data

import androidx.paging.PagingData
import dev.ikekazuma.marquee.core.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun nowPlayingPager(): Flow<PagingData<Movie>>
}
