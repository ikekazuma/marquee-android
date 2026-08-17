package dev.ikekazuma.marquee.feature.detail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.model.CastMember
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import dev.ikekazuma.marquee.core.designsystem.R as DesignSystemR

class DetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    private fun detail(
        overview: String = "あらすじ本文",
        cast: List<CastMember> = listOf(CastMember("俳優A", "役名A", null)),
        trailerKey: String? = "abc123",
    ) = MovieDetail(
        id = MovieId(1),
        title = "邦題タイトル",
        originalTitle = "Original Title",
        overview = overview,
        posterUrl = null,
        backdropUrl = null,
        releaseDate = LocalDate.of(2026, 8, 14),
        runtimeMinutes = 118,
        genres = listOf("ドラマ", "SF"),
        voteAverage = 7.8,
        cast = cast,
        trailerYouTubeKey = trailerKey,
    )

    @Test
    fun success_showsTitleRuntimeGenresAndCast() {
        composeTestRule.setContent {
            DetailScreen(uiState = DetailUiState.Success(detail()), onRetry = {})
        }

        composeTestRule.onNodeWithText("邦題タイトル").assertExists()
        composeTestRule.onNodeWithText("Original Title").assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.detail_runtime, 118)).assertExists()
        composeTestRule.onNodeWithText("ドラマ / SF").assertExists()
        composeTestRule.onNodeWithText("俳優A").assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.detail_watch_trailer)).assertExists()
    }

    @Test
    fun missingOverviewAndTrailer_showFallbacks() {
        composeTestRule.setContent {
            DetailScreen(uiState = DetailUiState.Success(detail(overview = "", trailerKey = null)), onRetry = {})
        }

        composeTestRule.onNodeWithText(context.getString(R.string.detail_overview_missing)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.detail_watch_trailer)).assertDoesNotExist()
    }

    @Test
    fun error_showsRetry() {
        composeTestRule.setContent {
            DetailScreen(uiState = DetailUiState.Error(AppError.Network), onRetry = {})
        }

        composeTestRule.onNodeWithText(context.getString(DesignSystemR.string.ds_retry)).assertExists()
    }
}
