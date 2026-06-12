package com.example.mynextfavoritegame.presentation.home

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynextfavoritegame.data.repository.GameRepository
import com.example.mynextfavoritegame.presentation.components.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private sealed interface FetchResult {
        data object Loading : FetchResult
        data class Success(val feature: Game?, val games: List<Game>) : FetchResult
        data class Error(val message: String) : FetchResult
    }

    private val _fetchResult = MutableStateFlow<FetchResult>(FetchResult.Loading)
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<HomeUiState> = combine(_fetchResult, _favorites) { result, favs ->
        when (result) {
            is FetchResult.Loading -> HomeUiState.Loading
            is FetchResult.Error -> HomeUiState.Error(result.message)
            is FetchResult.Success -> HomeUiState.Success(
                featuredGame = result.feature,
                games = result.games,
                favorites = favs
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState.Loading
    )

    init {
        loadGames()
    }

    fun loadGames(query: String = "games") {
        viewModelScope.launch {
            _fetchResult.value = FetchResult.Loading
            repository.searchGames(query).fold(
                onSuccess = { (featured, games) ->
                    _fetchResult.value = FetchResult.Success(featured, games)
                },
                onFailure = { error ->
                    _fetchResult.value = FetchResult.Error(
                        error.message ?: "Something went wrong"
                    )
                }
            )
        }
    }

    fun toggleFavorite(productId: String) {
        _favorites.update { current ->
            if (productId in current) current - productId else current + productId
        }
    }

}