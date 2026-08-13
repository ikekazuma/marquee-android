package dev.ikekazuma.marquee.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Fallback palette for devices without dynamic color. Marquee lights on a dark theater wall.
private val MarqueeAmber = Color(0xFFB8860B)
private val MarqueeAmberLight = Color(0xFFFFC94A)
private val MarqueeRed = Color(0xFF8E2A2A)
private val MarqueeRedLight = Color(0xFFFF8A80)

internal val LightColors = lightColorScheme(
    primary = MarqueeAmber,
    secondary = MarqueeRed,
)

internal val DarkColors = darkColorScheme(
    primary = MarqueeAmberLight,
    secondary = MarqueeRedLight,
)
