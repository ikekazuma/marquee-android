package dev.ikekazuma.marquee.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ikekazuma.marquee.core.common.AppError
import dev.ikekazuma.marquee.core.common.AppResult
import dev.ikekazuma.marquee.core.data.MovieRepository
import dev.ikekazuma.marquee.core.model.MovieDetail
import dev.ikekazuma.marquee.core.model.MovieId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data class Success(val detail: MovieDetail) : DetailUiState

    data class Error(val error: AppError) : DetailUiState
}

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
class DetailViewModel
    @AssistedInject
    constructor(
        // Dagger cannot generate a factory method taking a value class (name mangling), so the
        // assisted boundary uses the raw id and wraps it here.
        @Assisted private val rawMovieId: Int,
        private val repository: MovieRepository,
    ) : ViewModel() {
        private val movieId = MovieId(rawMovieId)

        private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
        val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

        init {
            load()
        }

        fun retry() = load()

        private fun load() {
            _uiState.value = DetailUiState.Loading
            viewModelScope.launch {
                _uiState.value =
                    when (val result = repository.movieDetail(movieId)) {
                        is AppResult.Success -> DetailUiState.Success(result.data)
                        is AppResult.Failure -> DetailUiState.Error(result.error)
                    }
            }
        }

        @AssistedFactory
        interface Factory {
            fun create(movieId: Int): DetailViewModel
        }
    }
