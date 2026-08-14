# Marquee

[![CI](https://github.com/ikekazuma/marquee-android/actions/workflows/ci.yml/badge.svg)](https://github.com/ikekazuma/marquee-android/actions/workflows/ci.yml)

Find what's playing in theaters across Japan.

> Work in progress. Built as a playground for modern Android development.

## Tech stack

- Kotlin / Jetpack Compose / Material 3
- Navigation 3
- Hilt, Room, Retrofit, Paging 3
- Multi-module + convention plugins
- Data: [TMDB](https://www.themoviedb.org/)

## Setup

Add your TMDB v4 Read Access Token to `local.properties`:

```
TMDB_ACCESS_TOKEN=your_token
```
