package com.example.mynextfavoritegame.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynextfavoritegame.presentation.components.Game
import com.example.mynextfavoritegame.presentation.components.GameCard
import com.example.mynextfavoritegame.ui.theme.MyNextFavoriteGameTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    games: List<Game>,
    onFavoriteClick: (String) -> Unit,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorites",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { innerPaddding ->
        if (games.isEmpty()) {
            EmptyFavoritesContent(
                modifier = Modifier
                    .padding(innerPaddding)
                    .fillMaxSize()
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(innerPaddding)
            ) {
                items(items = games, key = { it.productId }) { game ->
                    GameCard(
                        game = game,
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteClick(game.productId) },
                        onCardClick = { onGameClick(game) }
                    )
                }
            }
        }
    }
}


@Composable
private fun EmptyFavoritesContent(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(32.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No favorites yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the heart on any game to save it here.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "FavoritesScreen – With games")
@Composable
private fun FavoritesWithGamesPreview() {
    MyNextFavoriteGameTheme {
        FavoritesScreen(
            games = listOf(
                Game(
                    productId = "com.n3twork.tetris",
                    title = "Tetris®",
                    author = "PLAYSTUDIOS US, LLC",
                    rating = 4.1f,
                    reviews = 461000,
                    downloads = "50M+",
                    category = "Puzzle",
                    thumbnail = "",
                    heroImage = "",
                    screenshots = emptyList(),
                    description = "Play the world's most famous puzzle game.",
                    contentRating = "Everyone"
                )
            ),
            onFavoriteClick = {},
            onGameClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "FavoritesScreen – Empty")
@Composable
private fun FavoritesEmptyPreview() {
    MyNextFavoriteGameTheme {
        FavoritesScreen(
            games = emptyList(),
            onFavoriteClick = {},
            onGameClick = {}
        )
    }
}