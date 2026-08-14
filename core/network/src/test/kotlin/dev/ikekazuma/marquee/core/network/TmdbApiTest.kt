package dev.ikekazuma.marquee.core.network

import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class TmdbApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: TmdbApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(TmdbJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(TmdbApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun enqueue(fixtureName: String) {
        server.enqueue(MockResponse().setBody(fixture(fixtureName)))
    }

    @Test
    fun nowPlaying_parsesPagedList() =
        runTest {
            enqueue("now_playing.json")

            val page = api.nowPlaying(page = 1)

            assertEquals(1, page.page)
            assertEquals(4, page.totalPages)
            assertEquals(65, page.totalResults)
            assertEquals(5, page.results.size)
            val first = page.results.first()
            assertTrue(first.id > 0)
            assertTrue(first.title.isNotEmpty())
            assertTrue(first.posterPath!!.startsWith("/"))
        }

    @Test
    fun nowPlaying_sendsRegionAndLanguage() =
        runTest {
            enqueue("now_playing.json")

            api.nowPlaying(page = 3)

            val url = server.takeRequest().requestUrl!!
            assertEquals("movie/now_playing", url.encodedPath.trimStart('/'))
            assertEquals("3", url.queryParameter("page"))
            assertEquals("JP", url.queryParameter("region"))
            assertEquals("ja-JP", url.queryParameter("language"))
        }

    @Test
    fun nowPlaying_toleratesMissingAndEmptyFields() =
        runTest {
            enqueue("now_playing_sparse.json")

            val page = api.nowPlaying(page = 2)

            val noPoster = page.results[0]
            assertNull(noPoster.posterPath)
            assertEquals("", noPoster.releaseDate)

            val minimal = page.results[1]
            assertEquals("", minimal.overview)
            assertNull(minimal.releaseDate)
            assertEquals(0.0, minimal.voteAverage, 0.0)
        }

    @Test
    fun search_sendsQueryAndParsesResults() =
        runTest {
            enqueue("search.json")

            val page = api.search(query = "ゴジラ", page = 1)

            assertEquals("ゴジラ", server.takeRequest().requestUrl!!.queryParameter("query"))
            assertTrue(page.results.isNotEmpty())
        }

    @Test
    fun movieDetail_parsesCreditsAndVideos() =
        runTest {
            enqueue("movie_detail.json")

            val detail = api.movieDetail(id = 12345)

            assertEquals(145, detail.runtime)
            assertTrue(detail.genres.isNotEmpty())
            assertTrue(detail.credits!!.cast.isNotEmpty())
            val trailer = detail.videos!!.results.first { it.type == "Trailer" }
            assertEquals("YouTube", trailer.site)
            assertTrue(trailer.key.isNotEmpty())
        }

    @Test
    fun movieDetail_appendsCreditsAndVideos() =
        runTest {
            enqueue("movie_detail.json")

            api.movieDetail(id = 12345)

            val url = server.takeRequest().requestUrl!!
            assertEquals("movie/12345", url.encodedPath.trimStart('/'))
            assertEquals("credits,videos", url.queryParameter("append_to_response"))
        }
}
