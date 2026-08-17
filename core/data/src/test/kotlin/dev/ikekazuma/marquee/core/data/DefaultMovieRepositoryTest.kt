package dev.ikekazuma.marquee.core.data

import androidx.paging.testing.asSnapshot
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultMovieRepositoryTest {
    @Test
    fun nowPlayingPager_loadsOneTmdbPageInitially() =
        runTest {
            val remote = FakeMovieRemoteDataSource(totalPages = 3)
            val repository = DefaultMovieRepository(remote)

            val items = repository.nowPlayingPager().asSnapshot()

            assertEquals(20, items.size)
            assertEquals(listOf(1), remote.requestedPages)
            assertEquals(MovieId(1), items.first().id)
        }

    @Test
    fun nowPlayingPager_appendsNextPageWhenScrolledNearTheEnd() =
        runTest {
            val remote = FakeMovieRemoteDataSource(totalPages = 3)
            val repository = DefaultMovieRepository(remote)

            val items = repository.nowPlayingPager().asSnapshot { scrollTo(index = 19) }

            assertEquals(40, items.size)
            assertEquals(listOf(1, 2), remote.requestedPages)
            assertEquals(MovieId(40), items.last().id)
        }

    @Test
    fun nowPlayingPager_stopsAtLastPage() =
        runTest {
            val remote = FakeMovieRemoteDataSource(totalPages = 2)
            val repository = DefaultMovieRepository(remote)

            val items = repository.nowPlayingPager().asSnapshot { scrollTo(index = 39) }

            assertEquals(40, items.size)
            assertEquals(listOf(1, 2), remote.requestedPages)
        }

    @Test
    fun movieDetail_returnsRemoteResult() =
        runTest {
            val repository = DefaultMovieRepository(FakeMovieRemoteDataSource())

            val result = repository.movieDetail(MovieId(42))

            assertEquals(MovieId(42), (result as AppResult.Success).data.id)
        }

    @Test
    fun movieDetail_propagatesFailure() =
        runTest {
            val remote = FakeMovieRemoteDataSource().apply { failure = AppError.Network }
            val repository = DefaultMovieRepository(remote)

            val result = repository.movieDetail(MovieId(42))

            assertEquals(AppError.Network, (result as AppResult.Failure).error)
        }
}
