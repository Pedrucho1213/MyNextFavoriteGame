package com.example.mynextfavoritegame.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

private val previewGame = Game(
    productId = "com.example.game",
    title = "Tetris® Block Party",
    author = "PLAYSTUDIOS US, LLC",
    rating = 4.8f,
    reviews = 14000,
    downloads = "1M+",
    category = "Puzzle",
    thumbnail = "https://sm.ign.com/t/ign_latam/review/m/mario-kart/mario-kart-8-deluxe-review_vg8p.1280.jpg",
    heroImage = "https://www.muycomputer.com/wp-content/uploads/2022/08/Mario-Kart.jpg",
    screenshots = emptyList(),
    description = "Build, attack and steal with friends.",
    contentRating = "Everyone"
)

data class Game(
    val productId: String,
    val title: String,
    val author: String,
    val rating: Float,
    val reviews: Int,
    val downloads: String,
    val category: String,
    val thumbnail: String,
    val heroImage: String,
    val screenshots: List<String>,
    val description: String,
    val contentRating: String
)

@Composable
fun GameCard(
    game: Game,
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    ElevatedCard(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {

        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(game.heroImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            // Fila de Info

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Icono del juego
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(game.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                // Titulo, autor, raiting
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = game.author,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    RatingRow(rating = game.rating)
                }
                FavoriteIconButton(
                    isFavorite = isFavorite,
                    onClick = onFavoriteClick
                )
            }
        }

    }
}


@Preview(showBackground = false)
@Composable
private fun GameCardPreview() {

    GameCard(
        game = previewGame,
        isFavorite = false,
        onFavoriteClick = {},
        onCardClick = {}
    )
}

@Preview(showBackground = false)
@Composable
private fun GameCardFavoritePreview() {

    GameCard(
        game = previewGame,
        isFavorite = true,
        onFavoriteClick = {},
        onCardClick = {}
    )
}