package dev.ikekazuma.marquee.core.common

/**
 * Repositories return this instead of throwing, so exceptions never reach the UI layer.
 */
sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>

    data class Failure(val error: AppError) : AppResult<Nothing>
}

sealed interface AppError {
    data object Network : AppError

    // 401. The app ships a static read token, so the user cannot recover from this: do not offer retry.
    data object Unauthorized : AppError

    data class Http(val code: Int) : AppError

    data object Unknown : AppError
}
