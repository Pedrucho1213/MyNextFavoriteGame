package com.example.mynextfavoritegame.data.remote.mapper

import com.example.mynextfavoritegame.data.remote.dto.GameDto
import com.example.mynextfavoritegame.presentation.components.Game

fun GameDto.toDomain(): Game {
    val ext = extensions.orEmpty()
    return Game(
        productId = productId.orEmpty(),
        title = title.orEmpty(),
        author = author.orEmpty(),
        rating = rating?.toFloat() ?: 0f,
        reviews = reviews ?: 0,
        downloads = ext.firstOrNull { "downloads" in it.lowercase() } ?: "N/A",
        category = ext.firstOrNull {
            "downloads" !in it.lowercase() && "rated" !in it.lowercase()
        } ?: "Games",
        thumbnail = thumbnail.orEmpty(),
        heroImage = screenshots?.firstOrNull() ?: thumbnail.orEmpty(),
        screenshots = screenshots.orEmpty(),
        description = description.orEmpty(),
        contentRating = ext.firstOrNull { "rated" in it.lowercase() } ?: "Everyone"
    )
}