package dev.ikekazuma.marquee.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.designsystem.R
import dev.ikekazuma.marquee.core.designsystem.theme.MarqueeTheme

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

/**
 * [AppError.Unauthorized] means the shipped token is wrong, which no amount of retrying fixes,
 * so the retry button is hidden for it.
 */
@Composable
fun ErrorContent(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = error.message(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        if (error.isRetryable()) {
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.ds_retry))
            }
        }
    }
}

@Composable
private fun AppError.message(): String =
    when (this) {
        AppError.Network -> stringResource(R.string.ds_error_network)
        AppError.Unauthorized -> stringResource(R.string.ds_error_unauthorized)
        is AppError.Http -> stringResource(R.string.ds_error_http, code)
        AppError.Unknown -> stringResource(R.string.ds_error_unknown)
    }

private fun AppError.isRetryable(): Boolean = this != AppError.Unauthorized

@Preview
@Composable
private fun ErrorContentNetworkPreview() {
    MarqueeTheme(dynamicColor = false) {
        ErrorContent(error = AppError.Network, onRetry = {})
    }
}

@Preview
@Composable
private fun ErrorContentUnauthorizedPreview() {
    MarqueeTheme(dynamicColor = false) {
        ErrorContent(error = AppError.Unauthorized, onRetry = {})
    }
}
