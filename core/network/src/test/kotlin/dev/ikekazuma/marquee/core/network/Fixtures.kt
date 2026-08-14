package dev.ikekazuma.marquee.core.network

private object FixtureLoader

internal fun fixture(name: String): String =
    checkNotNull(FixtureLoader.javaClass.classLoader?.getResourceAsStream("fixtures/$name")) {
        "fixture not found: $name"
    }.bufferedReader().use { it.readText() }
