package com.example.mynextfavoritegame.data.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteIds(): Flow<Set<String>>
    suspend fun toggleFavorite(productId: String)
}
