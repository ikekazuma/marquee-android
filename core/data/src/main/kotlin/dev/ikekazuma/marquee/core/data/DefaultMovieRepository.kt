package dev.ikekazuma.marquee.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.ikekazuma.marquee.core.data.paging.MoviePagingSource
import dev.ikekazuma.marquee.core.data.paging.TMDB_PAGE_SIZE
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.network.MovieRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultMovieRepository
    @Inject
    constructor(private val remoteDataSource: MovieRemoteDataSource) :
    MovieRepository {
        override fun nowPlayingPager(): Flow<PagingData<Movie>> =
            Pager(
                // TMDB always returns 20 items per page, so initialLoadSize must not ask for more:
                // the default (3x pageSize) makes Paging keep loading until it reaches that count.
                config =
                    PagingConfig(
                        pageSize = TMDB_PAGE_SIZE,
                        initialLoadSize = TMDB_PAGE_SIZE,
                        prefetchDistance = PREFETCH_DISTANCE,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = { MoviePagingSource(remoteDataSource::nowPlaying) },
            ).flow

        private companion object {
            const val PREFETCH_DISTANCE = 5
        }
    }
