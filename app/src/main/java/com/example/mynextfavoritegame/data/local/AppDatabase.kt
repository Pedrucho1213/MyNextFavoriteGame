package com.example.mynextfavoritegame.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynextfavoritegame.data.local.dao.FavoriteDao
import com.example.mynextfavoritegame.data.local.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
