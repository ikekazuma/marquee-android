package dev.ikekazuma.marquee.core.common

/**
 * Paging reports failures as [Throwable]s, so an [AppError] has to travel wrapped.
 * The UI unwraps it again to decide what to show.
 */
class AppErrorException(val appError: AppError) : Exception(appError.toString())
