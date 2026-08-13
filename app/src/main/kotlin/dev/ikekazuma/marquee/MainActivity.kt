package dev.ikekazuma.marquee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.ikekazuma.marquee.core.designsystem.theme.MarqueeTheme
import dev.ikekazuma.marquee.ui.MarqueeApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MarqueeTheme {
                MarqueeApp()
            }
        }
    }
}
