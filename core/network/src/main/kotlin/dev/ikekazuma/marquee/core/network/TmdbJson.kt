package dev.ikekazuma.marquee.core.network

import kotlinx.serialization.json.Json

// TMDB keeps adding fields; only the ones the app declares are parsed.
internal val TmdbJson =
    Json {
        ignoreUnknownKeys = true
    }
