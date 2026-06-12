package com.example.mynextfavoritegame.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object Detail : Screen("detail/{productId}") {
        fun createRoute(productId: String) = "detail/$productId"
        const val ARG = "productId"
    }
}