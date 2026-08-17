package dev.ikekazuma.marquee.feature.detail

import androidx.paging.PagingData
import app.cash.turbine.test
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.data.MovieRepository
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

private fun movieDetail(id: Int) =
    MovieDetail(
        id = MovieId(id),
        title = "Movie $id",
        originalTitle = "Original $id",
        overview = "Overview",
        posterUrl = null,
        backdropUrl = null,
        releaseDate = LocalDate.of(2026, 8, 1),
        runtimeMinutes = 120,
        genres = listOf("ドラマ"),
        voteAverage = 7.5,
        cast = emptyList(),
        trailerYouTubeKey = null,
    )

private class FakeMovieRepository(var result: AppResult<MovieDetail> = AppResult.Success(movieDetail(1))) :
    MovieRepository {
    var callCount = 0
        private set

    override fun nowPlayingPager(): Flow<PagingData<Movie>> = flowOf(PagingData.empty())

    override suspend fun movieDetail(id: MovieId): AppResult<MovieDetail> {
        callCount++
        return result
    }
}

class DetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun uiState_startsLoadingThenEmitsSuccess() =
        runTest {
            val viewModel = DetailViewModel(1, FakeMovieRepository())

            viewModel.uiState.test {
                assertEquals(DetailUiState.Loading, awaitItem())
                assertEquals(movieDetail(1), (awaitItem() as DetailUiState.Success).detail)
            }
        }

    @Test
    fun uiState_emitsErrorOnFailure() =
        runTest {
            val repository = FakeMovieRepository(AppResult.Failure(AppError.Network))
            val viewModel = DetailViewModel(1, repository)

            viewModel.uiState.test {
                assertEquals(DetailUiState.Loading, awaitItem())
                assertEquals(AppError.Network, (awaitItem() as DetailUiState.Error).error)
            }
        }

    @Test
    fun retry_asksTheRepositoryAgain() =
        runTest {
            val repository = FakeMovieRepository(AppResult.Failure(AppError.Network))
            val viewModel = DetailViewModel(1, repository)

            viewModel.uiState.test {
                awaitItem()
                awaitItem()

                repository.result = AppResult.Success(movieDetail(1))
                viewModel.retry()

                assertEquals(DetailUiState.Loading, awaitItem())
                assertEquals(movieDetail(1), (awaitItem() as DetailUiState.Success).detail)
            }
            assertEquals(2, repository.callCount)
        }

    @Test
    fun requestsTheMovieItWasCreatedFor() =
        runTest {
            var requested: MovieId? = null
            val repository =
                object : MovieRepository {
                    override fun nowPlayingPager(): Flow<PagingData<Movie>> = flowOf(PagingData.empty())

                    override suspend fun movieDetail(id: MovieId): AppResult<MovieDetail> {
                        requested = id
                        return AppResult.Success(movieDetail(id.value))
                    }
                }

            DetailViewModel(99, repository).uiState.test {
                awaitItem()
                awaitItem()
            }

            assertEquals(MovieId(99), requested)
        }
}
