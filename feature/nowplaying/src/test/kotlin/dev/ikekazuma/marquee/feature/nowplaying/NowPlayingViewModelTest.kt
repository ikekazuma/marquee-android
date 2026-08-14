package dev.ikekazuma.marquee.feature.nowplaying

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NowPlayingViewModelTest {
    @Test
    fun movies_emitsPagingDataFromRepository() =
        runTest {
            val viewModel = NowPlayingViewModel(FakeMovieRepository())

            // Snapshot the emitted page, not the cachedIn flow itself: that one never completes.
            val items = flowOf(viewModel.movies.first()).asSnapshot()

            assertEquals(3, items.size)
            assertEquals(MovieId(1), items.first().id)
        }

    @Test
    fun movies_emitsASinglePagingDataAndKeepsTheStreamOpen() =
        runTest {
            val viewModel = NowPlayingViewModel(FakeMovieRepository())

            viewModel.movies.test {
                awaitItem()
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun movies_asksTheRepositoryOnlyOnceForMultipleCollectors() =
        runTest {
            val repository = FakeMovieRepository()
            val viewModel = NowPlayingViewModel(repository)

            viewModel.movies.first()
            viewModel.movies.first()

            assertEquals(1, repository.pagerCallCount)
        }
}
