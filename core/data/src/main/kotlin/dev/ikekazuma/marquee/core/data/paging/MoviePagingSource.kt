package dev.ikekazuma.marquee.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.ikekazuma.marquee.core.common.AppErrorException
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.model.Movie
import dev.ikekazuma.marquee.core.model.PagedMovies

internal const val TMDB_PAGE_SIZE = 20

/**
 * TMDB pages are 1-based and every list endpoint has the same shape, so the caller
 * only supplies how to fetch one page.
 */
internal class MoviePagingSource(private val fetch: suspend (page: Int) -> AppResult<PagedMovies>) :
    PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        return when (val result = fetch(page)) {
            is AppResult.Success -> {
                val paged = result.data
                LoadResult.Page(
                    data = paged.items,
                    prevKey = (page - 1).takeIf { it >= FIRST_PAGE },
                    nextKey = (page + 1).takeIf { page < paged.totalPages },
                )
            }

            is AppResult.Failure -> LoadResult.Error(AppErrorException(result.error))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchor ->
            val closest = state.closestPageToPosition(anchor)
            closest?.prevKey?.plus(1) ?: closest?.nextKey?.minus(1)
        }

    private companion object {
        const val FIRST_PAGE = 1
    }
}
