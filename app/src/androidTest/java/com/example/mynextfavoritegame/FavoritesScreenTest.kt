package com.example.mynextfavoritegame

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mynextfavoritegame.domain.model.Game
import com.example.mynextfavoritegame.presentation.favorites.FavoritesScreen
import com.example.mynextfavoritegame.ui.theme.MyNextFavoriteGameTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.collections.emptyList

@RunWith(AndroidJUnit4::class)
class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeGame = Game(
        productId = "com.test.game",
        title = "Test Game",
        author = "Test Author",
        rating = 4.5f,
        reviews = 1000,
        downloads = "1M+",
        category = "Puzzle",
        thumbnail = "",
        heroImage = "",
        screenshots = emptyList(),
        description = "Test description",
        contentRating = "Everyone"
    )

    @Test
    fun emptyStateShowNoFavoritesMessage(){
        composeTestRule.setContent {
            MyNextFavoriteGameTheme {
                FavoritesScreen(
                    games = emptyList(),
                    onFavoriteClick = {},
                    onGameClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("No favorites yet").assertIsDisplayed()
    }

    @Test
    fun withGamesShowGameTitle(){
        composeTestRule.setContent {
            MyNextFavoriteGameTheme {
                FavoritesScreen(
                    games = listOf(fakeGame),
                    onFavoriteClick = {},
                    onGameClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Test Game").assertIsDisplayed()
    }
}