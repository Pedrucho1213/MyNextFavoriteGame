package com.example.mynextfavoritegame.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynextfavoritegame.data.repository.FavoritesRepository
import com.example.mynextfavoritegame.data.repository.GameRepository
import com.example.mynextfavoritegame.presentation.components.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private sealed interface FetchResult {
        data object Loading : FetchResult
        data class Success(val featured: Game?, val games: List<Game>) : FetchResult
        data class Error(val message: String) : FetchResult
    }

    private val _searchQuery = MutableStateFlow("games")
    private val _retryCount = MutableStateFlow(0)

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _fetchResult: StateFlow<FetchResult> = combine(_searchQuery, _retryCount) { query, _ -> query }
        .debounce(600L)
        .flatMapLatest { query ->
            flow {
                emit(FetchResult.Loading)
                gameRepository.searchGames(query.ifBlank { "games" }).fold(
                    onSuccess = { (featured, games) ->
                        emit(FetchResult.Success(featured, games))
                    },
                    onFailure = { error ->
                        emit(FetchResult.Error(error.message ?: "Something went wrong"))
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = FetchResult.Loading
        )

    private val _favorites: StateFlow<Set<String>> = favoritesRepository
        .getFavoriteIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptySet()
        )

    val uiState: StateFlow<HomeUiState> = combine(_fetchResult, _favorites) { result, favs ->
        when (result) {
            is FetchResult.Loading -> HomeUiState.Loading
            is FetchResult.Error -> HomeUiState.Error(result.message)
            is FetchResult.Success -> HomeUiState.Success(
                featuredGame = result.featured,
                games = result.games,
                favorites = favs
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState.Loading
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun retry() {
        _retryCount.value++
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(productId)
        }
    }
}
