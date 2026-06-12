package com.example.mynextfavoritegame.data.remote

import com.example.mynextfavoritegame.data.remote.dto.SerpApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SerpApiService {

    @GET("search.json")
    suspend fun searchGames(
        @Query("engine") engine: String = "google_play_games",
        @Query("q") query: String
    ): SerpApiResponse
}