package com.example.mynextfavoritegame.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
fun HomeScreen(
    uiState: HomeUiState,
    onFavoriteClick: (String) -> Unit,
    onGameClick: (Game) -> Unit,
    onRetry: () -> Unit,
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
        when (uiState){
            is HomeUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
            is HomeUiState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    onRetry = onRetry,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                )
            }
            is HomeUiState.Success -> {
                SuccessContent(
                    featuredGame = uiState.featuredGame,
                    games = uiState.games,
                    favorites = uiState.favorites,
                    onFavoriteClick = onFavoriteClick,
                    onGameClick = onGameClick,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun SuccessContent(
    featuredGame: Game?,
    games: List<Game>,
    favorites: Set<String>,
    onFavoriteClick: (String)-> Unit,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier,
){
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
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


@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(24.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator()
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



@Preview(showBackground = true, showSystemUi = true, name = "HomeScreen – Success")
@Composable
private fun HomeScreenSuccessPreview() {
    MyNextFavoriteGameTheme {
        HomeScreen(
            uiState = HomeUiState.Success(
                featuredGame = fakeFeatured,
                games = fakeGames,
                favorites = setOf("com.n3twork.tetris")
            ),
            onFavoriteClick = {},
            onGameClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "HomeScreen – Loading")
@Composable
private fun HomeScreenLoadingPreview() {
    MyNextFavoriteGameTheme {
        HomeScreen(
            uiState = HomeUiState.Loading,
            onFavoriteClick = {},
            onGameClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "HomeScreen – Error")
@Composable
private fun HomeScreenErrorPreview() {
    MyNextFavoriteGameTheme {
        HomeScreen(
            uiState = HomeUiState.Error("Something went wrong. Check your connection."),
            onFavoriteClick = {},
            onGameClick = {},
            onRetry = {}
        )
    }
}