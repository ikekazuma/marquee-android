package dev.ikekazuma.marquee.core.data.paging

import androidx.paging.PagingSource
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppErrorException
import dev.ikekazuma.marquee.core.data.FakeMovieRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MoviePagingSourceTest {
    private fun refresh(key: Int? = null) =
        PagingSource.LoadParams.Refresh<Int>(
            key = key,
            loadSize = TMDB_PAGE_SIZE,
            placeholdersEnabled = false,
        )

    @Test
    fun firstLoad_startsAtPageOneAndPointsToNextPage() =
        runTest {
            val remote = FakeMovieRemoteDataSource(totalPages = 3)
            val source = MoviePagingSource(remote::nowPlaying)

            val result = source.load(refresh()) as PagingSource.LoadResult.Page

            assertEquals(listOf(1), remote.requestedPages)
            assertEquals(TMDB_PAGE_SIZE, result.data.size)
            assertNull(result.prevKey)
            assertEquals(2, result.nextKey)
        }

    @Test
    fun middlePage_pointsBothWays() =
        runTest {
            val source = MoviePagingSource(FakeMovieRemoteDataSource(totalPages = 3)::nowPlaying)

            val result = source.load(refresh(key = 2)) as PagingSource.LoadResult.Page

            assertEquals(1, result.prevKey)
            assertEquals(3, result.nextKey)
        }

    @Test
    fun lastPage_hasNoNextKey() =
        runTest {
            val source = MoviePagingSource(FakeMovieRemoteDataSource(totalPages = 3)::nowPlaying)

            val result = source.load(refresh(key = 3)) as PagingSource.LoadResult.Page

            assertNull(result.nextKey)
        }

    @Test
    fun failure_isReportedAsAppErrorException() =
        runTest {
            val remote = FakeMovieRemoteDataSource().apply { failure = AppError.Network }
            val source = MoviePagingSource(remote::nowPlaying)

            val result = source.load(refresh()) as PagingSource.LoadResult.Error

            val error = result.throwable
            assertTrue(error is AppErrorException)
            assertEquals(AppError.Network, (error as AppErrorException).appError)
        }
}
