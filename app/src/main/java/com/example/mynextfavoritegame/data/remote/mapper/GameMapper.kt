package com.example.mynextfavoritegame.data.remote.mapper

import com.example.mynextfavoritegame.data.remote.dto.GameDto
import com.example.mynextfavoritegame.presentation.components.Game

fun GameDto.toDomain(): Game {
    // El thumbnail de Google Play termina en =s64-rw (64px).
    // Sustituimos por s512 para obtener una imagen de mayor resolución para el hero.
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
