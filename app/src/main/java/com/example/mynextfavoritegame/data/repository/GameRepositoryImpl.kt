package com.example.mynextfavoritegame.data.repository

import com.example.mynextfavoritegame.data.remote.SerpApiService
import com.example.mynextfavoritegame.data.remote.mapper.toDomain
import com.example.mynextfavoritegame.presentation.components.Game
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val service: SerpApiService
) : GameRepository {

    override suspend fun searchGames(query: String): Result<Pair<Game?, List<Game>>> {
        return runCatching {
            val response = service.searchGames(query = query)
            val featured = response.appHighlight
                ?.takeIf { !it.productId.isNullOrBlank() && !it.title.isNullOrBlank() }
                ?.toDomain()
            val games = response.organicResults
                ?.filter { !it.productId.isNullOrBlank() && !it.title.isNullOrBlank() }
                ?.map { it.toDomain() }
                ?: emptyList()
            Pair(featured, games)
        }
    }
}