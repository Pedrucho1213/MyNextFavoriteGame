package com.example.mynextfavoritegame.data.remote.mapper

import com.example.mynextfavoritegame.data.remote.dto.GameDto
import com.example.mynextfavoritegame.domain.model.Game

fun GameDto.toDomain(): Game {
    val heroImage = thumbnail?.replace("=s64-rw", "=s512-rw") ?: thumbnail.orEmpty()

    return Game(
        productId = productId.orEmpty(),
        title = title.orEmpty(),
        author = author.orEmpty(),
        rating = rating?.toFloat() ?: 0f,
        reviews = 0,
        downloads = downloads ?: "N/A",
        category = category ?: "Games",
        thumbnail = thumbnail.orEmpty(),
        heroImage = heroImage,
        screenshots = emptyList(),
        description = description.orEmpty(),
        contentRating = "Everyone"
    )
}
