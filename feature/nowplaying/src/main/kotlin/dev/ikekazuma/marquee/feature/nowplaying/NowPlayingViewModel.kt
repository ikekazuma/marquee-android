package dev.ikekazuma.marquee.feature.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ikekazuma.marquee.core.data.MovieRepository
import dev.ikekazuma.marquee.core.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel
    @Inject
    constructor(repository: MovieRepository) : ViewModel() {
        // cachedIn keeps the loaded pages across configuration changes and multiple collectors.
        val movies: Flow<PagingData<Movie>> = repository.nowPlayingPager().cachedIn(viewModelScope)
    }
