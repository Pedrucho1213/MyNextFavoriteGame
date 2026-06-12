package com.example.mynextfavoritegame.presentation.main

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mynextfavoritegame.navigation.Screen
import com.example.mynextfavoritegame.presentation.detail.DetailScreen
import com.example.mynextfavoritegame.presentation.favorites.FavoritesScreen
import com.example.mynextfavoritegame.presentation.home.HomeScreen
import com.example.mynextfavoritegame.presentation.home.HomeUiState
import com.example.mynextfavoritegame.presentation.home.HomeViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                    uiState = uiState,
                    onFavoriteClick = viewModel::toggleFavorite,
                    onGameClick = { game ->
                        navController.navigate(Screen.Detail.createRoute(game.productId))
                    },
                    onRetry = { viewModel.loadGames() }
                )
            }

            composable(Screen.Favorites.route) {
                val favoriteGames = when (val s = uiState) {
                    is HomeUiState.Success -> {
                        val all = listOfNotNull(s.featuredGame) + s.games
                        all.filter { it.productId in s.favorites }
                    }
                    else -> emptyList()
                }
                FavoritesScreen(
                    games = favoriteGames,
                    onFavoriteClick = viewModel::toggleFavorite,
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
                val game = when (val s = uiState) {
                    is HomeUiState.Success -> {
                        val all = listOfNotNull(s.featuredGame) + s.games
                        all.find { it.productId == productId }
                    }
                    else -> null
                }
                if (game != null) {
                    DetailScreen(
                        game = game,
                        isFavorite = when (val s = uiState) {
                            is HomeUiState.Success -> productId in s.favorites
                            else -> false
                        },
                        onFavoriteClick = {
                            if (productId != null) viewModel.toggleFavorite(productId)
                        },
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}