package com.example.mynextfavoritegame.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynextfavoritegame.presentation.components.Game
import com.example.mynextfavoritegame.presentation.components.GameCard
import com.example.mynextfavoritegame.ui.theme.MyNextFavoriteGameTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    featuredGame: Game?,
    games: List<Game>,
    favorites: Set<String>,
    onFavoriteClick: (String) -> Unit,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Games",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            featuredGame?.let { game ->
                item(key = "label_featured") {
                    SectionLabel(text = "Featured")
                }
                item(key = "featured ${game.productId}") {
                    GameCard(
                        game = game,
                        isFavorite = favorites.contains(game.productId),
                        onFavoriteClick = { onFavoriteClick(game.productId) },
                        onCardClick = { onGameClick(game) }
                    )
                }
                if (games.isNotEmpty()) {
                    item(key = "label_results") {
                        SectionLabel(text = "More games")
                    }
                    items(
                        items = games,
                        key = { it.productId }
                    ) { game ->
                        GameCard(
                            game = game,
                            isFavorite = favorites.contains(game.productId),
                            onFavoriteClick = { onFavoriteClick(game.productId) },
                            onCardClick = { onGameClick(game) }
                        )
                    }
                }
            }
        }
    }
}

// ——— Preview data ———

private val fakeGames = listOf(
    Game(
        productId = "com.example.one",
        title = "Tetris® Block Blast",
        author = "PLAYSTUDIOS US, LLC",
        rating = 4.2f,
        reviews = 82000,
        downloads = "1M+",
        category = "Puzzle",
        thumbnail = "",
        heroImage = "",
        screenshots = emptyList(),
        description = "The ultimate block puzzle game.",
        contentRating = "Everyone"
    ),
    Game(
        productId = "com.example.two",
        title = "Tetris® Block Party",
        author = "PLAYSTUDIOS US, LLC",
        rating = 4.8f,
        reviews = 14000,
        downloads = "1M+",
        category = "Puzzle",
        thumbnail = "",
        heroImage = "",
        screenshots = emptyList(),
        description = "Build, attack and steal with friends.",
        contentRating = "Everyone"
    )
)

private val fakeFeatured = fakeGames.first()

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "HomeScreen – Light")
@Composable
private fun HomeScreenPreview() {
    MyNextFavoriteGameTheme(darkTheme = false) {
        HomeScreen(
            featuredGame = fakeFeatured,
            games = fakeGames.drop(1),
            favorites = setOf("com.example.one"),
            onFavoriteClick = {},
            onGameClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "HomeScreen – Dark")
@Composable
private fun HomeScreenDarkPreview() {
    MyNextFavoriteGameTheme(darkTheme = true) {
        HomeScreen(
            featuredGame = fakeFeatured,
            games = fakeGames.drop(1),
            favorites = emptySet(),
            onFavoriteClick = {},
            onGameClick = {}
        )
    }
}

