package dev.ikekazuma.marquee.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.ikekazuma.marquee.R
import dev.ikekazuma.marquee.core.designsystem.theme.MarqueeTheme

@Composable
fun MarqueeApp() {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.app_placeholder))
        }
    }
}

@Preview
@Composable
private fun MarqueeAppPreview() {
    MarqueeTheme(dynamicColor = false) {
        MarqueeApp()
    }
}
