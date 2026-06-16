package com.example.mynextfavoritegame.data.repository

import com.example.mynextfavoritegame.domain.model.Game

interface GameRepository {
    suspend fun searchGames(query: String): Result<Pair<Game?, List<Game>>>
}