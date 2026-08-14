package dev.ikekazuma.marquee.feature.nowplaying

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.platform.app.InstrumentationRegistry
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppErrorException
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import dev.ikekazuma.marquee.core.designsystem.R as DesignSystemR

class NowPlayingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    private fun movie(id: Int) =
        Movie(
            id = MovieId(id),
            title = "映画 $id",
            posterUrl = null,
            releaseDate = LocalDate.of(2026, 8, 14),
            voteAverage = 7.5,
        )

    private fun setScreen(pagingData: PagingData<Movie>) {
        composeTestRule.setContent {
            Screen(pagingData)
        }
    }

    @Composable
    private fun Screen(pagingData: PagingData<Movie>) {
        NowPlayingScreen(
            movies = flowOf(pagingData).collectAsLazyPagingItems(),
            onMovieClick = {},
        )
    }

    private fun loadedPagingData(movies: List<Movie>): PagingData<Movie> =
        PagingData.from(
            data = movies,
            sourceLoadStates =
                LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                ),
        )

    private fun errorPagingData(error: AppError): PagingData<Movie> =
        PagingData.empty(
            sourceLoadStates =
                LoadStates(
                    refresh = LoadState.Error(AppErrorException(error)),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                ),
        )

    @Test
    fun showsLoadedMovies() {
        setScreen(loadedPagingData(listOf(movie(1), movie(2))))

        composeTestRule.onNodeWithText("映画 1").assertExists()
        composeTestRule.onNodeWithText("映画 2").assertExists()
    }

    @Test
    fun networkError_showsMessageWithRetry() {
        setScreen(errorPagingData(AppError.Network))

        composeTestRule.onNodeWithText(context.getString(DesignSystemR.string.ds_error_network)).assertExists()
        composeTestRule.onNodeWithText(context.getString(DesignSystemR.string.ds_retry)).assertExists()
    }

    @Test
    fun unauthorizedError_hidesRetry() {
        setScreen(errorPagingData(AppError.Unauthorized))

        composeTestRule.onNodeWithText(context.getString(DesignSystemR.string.ds_error_unauthorized)).assertExists()
        composeTestRule.onNodeWithText(context.getString(DesignSystemR.string.ds_retry)).assertDoesNotExist()
    }

    @Test
    fun emptyResult_showsEmptyMessage() {
        setScreen(loadedPagingData(emptyList()))

        composeTestRule.onNodeWithText(context.getString(R.string.nowplaying_empty)).assertExists()
    }
}
