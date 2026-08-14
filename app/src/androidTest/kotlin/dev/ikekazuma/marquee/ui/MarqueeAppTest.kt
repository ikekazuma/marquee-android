package dev.ikekazuma.marquee.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import dev.ikekazuma.marquee.R
import org.junit.Rule
import org.junit.Test

class MarqueeAppTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsPlaceholder() {
        val placeholder =
            InstrumentationRegistry
                .getInstrumentation()
                .targetContext
                .getString(R.string.app_placeholder)

        composeTestRule.setContent { MarqueeApp() }

        composeTestRule.onNodeWithText(placeholder).assertExists()
    }
}
