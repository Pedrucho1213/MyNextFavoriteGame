package com.example.mynextfavoritegame.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column {
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
                if (uiState is HomeUiState.Success) {
                    SearchField(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        when (uiState) {
            is HomeUiState.Loading -> LoadingContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
            is HomeUiState.Error -> ErrorContent(
                message = uiState.message,
                onRetry = onRetry,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
            is HomeUiState.Success -> SuccessContent(
                featuredGame = uiState.featuredGame,
                games = uiState.games,
                favorites = uiState.favorites,
                searchQuery = searchQuery,
                onFavoriteClick = onFavoriteClick,
                onGameClick = onGameClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = { if (it.length <= 60) onQueryChange(it) },
        placeholder = {
            Text(
                text = "Search by title or developer…",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        shape = RoundedCornerShape(28.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier
    )
}

// ─── Success content ───────────────────────────────────────────────────────

@Composable
private fun SuccessContent(
    featuredGame: Game?,
    games: List<Game>,
    favorites: Set<String>,
    searchQuery: String,
    onFavoriteClick: (String) -> Unit,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val trimmedQuery = searchQuery.trim()
    val isSearching = trimmedQuery.length >= 2

    val filteredGames by remember(games) {
        derivedStateOf {
            if (!isSearching) games
            else games.filter { game ->
                game.title.contains(trimmedQuery, ignoreCase = true) ||
                        game.author.contains(trimmedQuery, ignoreCase = true)
            }
        }
    }

    val filteredFeatured by remember(featuredGame) {
        derivedStateOf {
            if (!isSearching) featuredGame
            else if (featuredGame != null &&
                (featuredGame.title.contains(trimmedQuery, ignoreCase = true) ||
                        featuredGame.author.contains(trimmedQuery, ignoreCase = true))
            ) featuredGame
            else null
        }
    }

    val hasResults = filteredFeatured != null || filteredGames.isNotEmpty()

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
        if (isSearching && !hasResults) {
            item {
                EmptySearchContent(
                    query = trimmedQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp)
                )
            }
        } else {
            filteredFeatured?.let { game ->
                item(key = "label_featured") {
                    SectionLabel(text = if (isSearching) "Result in Featured" else "Featured")
                }
                item(key = "featured_${game.productId}") {
                    GameCard(
                        game = game,
                        isFavorite = favorites.contains(game.productId),
                        onFavoriteClick = { onFavoriteClick(game.productId) },
                        onCardClick = { onGameClick(game) }
                    )
                }
            }

            if (filteredGames.isNotEmpty()) {
                item(key = "label_results") {
                    SectionLabel(
                        text = if (isSearching) "Results (${filteredGames.size})" else "More games"
                    )
                }
                items(items = filteredGames, key = { it.productId }) { game ->
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
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator()
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
            modifier = Modifier.size(48.dp)
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
            Text("Retry")
        }
    }
}


@Composable
private fun EmptySearchContent(
    query: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(52.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No results for \"$query\"",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Try a different title or developer name.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}


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


@Preview(showBackground = true, showSystemUi = true, name = "HomeScreen – Success")
@Composable
private fun HomeScreenSuccessPreview() {
    MyNextFavoriteGameTheme {
        HomeScreen(
            uiState = HomeUiState.Success(
                featuredGame = fakeFeatured,
                games = fakeGames,
                favorites = setOf("com.example.one")
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
