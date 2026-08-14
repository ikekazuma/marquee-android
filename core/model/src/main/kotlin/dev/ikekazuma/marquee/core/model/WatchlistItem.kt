package dev.ikekazuma.marquee.core.model

import java.time.Instant

data class WatchlistItem(
    val movie: Movie,
    val addedAt: Instant,
)
