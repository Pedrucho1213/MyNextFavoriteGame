package com.example.mynextfavoritegame.domain.model

data class Game(
    val productId: String,
    val title: String,
    val author: String,
    val rating: Float,
    val reviews: Int,
    val downloads: String,
    val category: String,
    val thumbnail: String,
    val heroImage: String,
    val screenshots: List<String>,
    val description: String,
    val contentRating: String
)
