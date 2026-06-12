package com.example.mynextfavoritegame.data.repository

import com.example.mynextfavoritegame.data.local.dao.FavoriteDao
import com.example.mynextfavoritegame.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoritesRepository {

    override fun getFavoriteIds(): Flow<Set<String>> =
        dao.getAllFavorites().map { list -> list.map { it.productId }.toSet() }

    override suspend fun toggleFavorite(productId: String) {
        if (dao.isFavorite(productId)) {
            dao.delete(productId)
        } else {
            dao.insert(FavoriteEntity(productId))
        }
    }
}
