package com.example.mynextfavoritegame.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mynextfavoritegame.navigation.Screen
import com.example.mynextfavoritegame.presentation.components.Game
import com.example.mynextfavoritegame.presentation.detail.DetailScreen
import com.example.mynextfavoritegame.presentation.favorites.FavoritesScreen
import com.example.mynextfavoritegame.presentation.home.HomeScreen
import com.example.mynextfavoritegame.presentation.home.HomeUiState


// — Fake data hasta conectar el ViewModel —
private val fakeFeatured = Game(
    productId = "com.n3twork.tetris",
    title = "Tetris®",
    author = "PLAYSTUDIOS US, LLC",
    rating = 4.1f,
    reviews = 461000,
    downloads = "50M+",
    category = "Puzzle",
    thumbnail = "https://play-lh.googleusercontent.com/oNaKkMEDGWMJF8xOiRdxFnj_UMamqhYP9Q2t0xhxEq8JfBQHGnhHwHMNTGu8SKVJ8g",
    heroImage = "https://play-lh.googleusercontent.com/oNaKkMEDGWMJF8xOiRdxFnj_UMamqhYP9Q2t0xhxEq8JfBQHGnhHwHMNTGu8SKVJ8g",
    screenshots = emptyList(),
    description = "Play the world's most famous puzzle game.",
    contentRating = "Everyone"
)
private val fakeGames = listOf(
    Game(
        productId = "com.playstudios.blockblast",
        title = "Block Blast!",
        author = "Hungry Studio",
        rating = 4.7f,
        reviews = 1200000,
        downloads = "100M+",
        category = "Puzzle",
        thumbnail = "https://play-lh.googleusercontent.com/oNaKkMEDGWMJF8xOiRdxFnj_UMamqhYP9Q2t0xhxEq8JfBQHGnhHwHMNTGu8SKVJ8g",
        heroImage = "https://play-lh.googleusercontent.com/oNaKkMEDGWMJF8xOiRdxFnj_UMamqhYP9Q2t0xhxEq8JfBQHGnhHwHMNTGu8SKVJ8g",
        screenshots = emptyList(),
        description = "Drop blocks to fill rows and columns.",
        contentRating = "Everyone"
    ),
    Game(
        productId = "com.supercell.clashofclans",
        title = "Clash of Clans",
        author = "Supercell",
        rating = 4.6f,
        reviews = 22000000,
        downloads = "500M+",
        category = "Strategy",
        thumbnail = "https://play-lh.googleusercontent.com/oNaKkMEDGWMJF8xOiRdxFnj_UMamqhYP9Q2t0xhxEq8JfBQHGnhHwHMNTGu8SKVJ8g",
        heroImage = "https://play-lh.googleusercontent.com/oNaKkMEDGWMJF8xOiRdxFnj_UMamqhYP9Q2t0xhxEq8JfBQHGnhHwHMNTGu8SKVJ8g",
        screenshots = emptyList(),
        description = "Build your village, train your troops, and battle with others.",
        contentRating = "Everyone 10+"
    )
)

private val allGames = listOf(fakeFeatured) + fakeGames

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // TODO: reemplazar con ViewModel cuando esté listo
    var favorites by remember { mutableStateOf(emptySet<String>()) }

    val bottomNavItems = listOf(Screen.Home, Screen.Favorites)
    val showBottomBar = bottomNavItems.any {
        currentDestination?.hierarchy?.any { dest -> dest.route == it.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        val selected = currentDestination?.hierarchy
                            ?.any { it.route == screen.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                when (screen) {
                                    Screen.Home -> Icon(
                                        imageVector = if (selected) Icons.Filled.Home else Icons.Outlined.Home,
                                        contentDescription = "Games"
                                    )
                                    Screen.Favorites -> Icon(
                                        imageVector = if (selected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Favorites"
                                    )
                                    else -> {}
                                }
                            },
                            label = {
                                Text(
                                    text = when (screen) {
                                        Screen.Home -> "Games"
                                        Screen.Favorites -> "Favorites"
                                        else -> ""
                                    }
                                )
                            }
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    uiState = HomeUiState.Success(
                        featuredGame = fakeFeatured,
                        games = fakeGames,
                        favorites = favorites
                    ),
                    onFavoriteClick = { id ->
                        favorites = if (id in favorites) favorites - id else favorites + id
                    },
                    onGameClick = { game ->
                        navController.navigate(Screen.Detail.createRoute(game.productId))
                    },
                    onRetry = {},
                    modifier = Modifier
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    games = allGames.filter { it.productId in favorites },
                    onFavoriteClick = { id ->
                        favorites = if (id in favorites) favorites - id else favorites + id
                    },
                    onGameClick = { game ->
                        navController.navigate(Screen.Detail.createRoute(game.productId))
                    }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument(Screen.Detail.ARG) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString(Screen.Detail.ARG)
                val game = allGames.find { it.productId == productId }
                if (game != null) {
                    DetailScreen(
                        game = game,
                        isFavorite = productId in favorites,
                        onFavoriteClick = {
                            if (productId != null) {
                                favorites = if (productId in favorites) {
                                    favorites - productId
                                } else {
                                    favorites + productId
                                }
                            }
                        },
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}