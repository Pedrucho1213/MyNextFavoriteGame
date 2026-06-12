package com.example.mynextfavoritegame.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerpApiResponse(
    @Json(name = "app_highlight") val appHighlight: GameDto?,
    @Json(name = "organic_results") val organicResults: List<OrganicResultGroup>?
)

@JsonClass(generateAdapter = true)
data class OrganicResultGroup(
    @Json(name = "items") val items: List<GameDto>?
)

@JsonClass(generateAdapter = true)
data class GameDto(
    @Json(name = "product_id") val productId: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "author") val author: String?,
    @Json(name = "rating") val rating: Double?,
    @Json(name = "category") val category: String?,
    @Json(name = "downloads") val downloads: String?,
    @Json(name = "thumbnail") val thumbnail: String?,
    @Json(name = "description") val description: String?
)
