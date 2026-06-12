package com.example.mynextfavoritegame.presentation.home

import com.example.mynextfavoritegame.presentation.components.Game

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val featuredGame: Game?,
        val games: List<Game>,
        val favorites: Set<String>
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}