package dev.ikekazuma.marquee.core.network

import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.LocalDate

class RetrofitMovieRemoteDataSourceTest {
    private lateinit var server: MockWebServer
    private lateinit var dataSource: MovieRemoteDataSource

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        val api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor("test-token")).build())
                .addConverterFactory(TmdbJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(TmdbApi::class.java)
        dataSource = RetrofitMovieRemoteDataSource(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun enqueueFixture(name: String) {
        server.enqueue(MockResponse().setBody(fixture(name)))
    }

    @Test
    fun nowPlaying_mapsToDomain() =
        runTest {
            enqueueFixture("now_playing.json")

            val result = dataSource.nowPlaying(page = 1)

            val paged = (result as AppResult.Success).data
            assertEquals(1, paged.page)
            assertEquals(4, paged.totalPages)
            assertEquals(5, paged.items.size)
            val movie = paged.items.first()
            assertTrue(movie.posterUrl!!.startsWith("https://image.tmdb.org/t/p/w500/"))
            assertTrue(movie.title.isNotEmpty())
        }

    @Test
    fun nowPlaying_sendsBearerToken() =
        runTest {
            enqueueFixture("now_playing.json")

            dataSource.nowPlaying(page = 1)

            assertEquals("Bearer test-token", server.takeRequest().getHeader("Authorization"))
        }

    @Test
    fun nowPlaying_mapsMissingPosterAndBlankReleaseDate() =
        runTest {
            enqueueFixture("now_playing_sparse.json")

            val result = dataSource.nowPlaying(page = 2)

            val items = (result as AppResult.Success).data.items
            assertNull(items[0].posterUrl)
            assertNull(items[0].releaseDate)
            assertEquals("最小構成", items[1].title)
        }

    @Test
    fun movieDetail_mapsCastGenresAndTrailer() =
        runTest {
            enqueueFixture("movie_detail.json")

            val result = dataSource.movieDetail(MovieId(12345))

            val detail = (result as AppResult.Success).data
            assertEquals(145, detail.runtimeMinutes)
            assertTrue(detail.genres.isNotEmpty())
            assertEquals("トム・ホランド", detail.cast.first().name)
            assertTrue(
                detail.cast
                    .first()
                    .profileUrl!!
                    .startsWith("https://image.tmdb.org/t/p/w185/"),
            )
            assertEquals("F358F3Nso3o", detail.trailerYouTubeKey)
            assertEquals(LocalDate.parse("2026-07-29"), detail.releaseDate)
        }

    @Test
    fun httpErrors_mapToHttpAppError() =
        runTest {
            listOf(404, 429, 500).forEach { code ->
                server.enqueue(MockResponse().setResponseCode(code).setBody("{}"))

                val result = dataSource.nowPlaying(page = 1)

                assertEquals(AppError.Http(code), (result as AppResult.Failure).error)
            }
        }

    @Test
    fun unauthorized_mapsToUnauthorizedAppError() =
        runTest {
            server.enqueue(MockResponse().setResponseCode(401).setBody("{}"))

            val result = dataSource.nowPlaying(page = 1)

            assertEquals(AppError.Unauthorized, (result as AppResult.Failure).error)
        }

    @Test
    fun connectionFailure_mapsToNetworkAppError() =
        runTest {
            server.enqueue(MockResponse().apply { socketPolicy = SocketPolicy.DISCONNECT_AT_START })

            val result = dataSource.nowPlaying(page = 1)

            assertEquals(AppError.Network, (result as AppResult.Failure).error)
        }

    @Test
    fun malformedJson_mapsToUnknownAppError() =
        runTest {
            server.enqueue(MockResponse().setBody("""{"page": "not a number"}"""))

            val result = dataSource.nowPlaying(page = 1)

            assertEquals(AppError.Unknown, (result as AppResult.Failure).error)
        }
}
