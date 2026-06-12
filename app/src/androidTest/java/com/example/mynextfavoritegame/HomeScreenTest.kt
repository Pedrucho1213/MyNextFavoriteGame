package com.example.mynextfavoritegame

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mynextfavoritegame.presentation.home.HomeScreen
import com.example.mynextfavoritegame.presentation.home.HomeUiState
import com.example.mynextfavoritegame.ui.theme.MyNextFavoriteGameTheme
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorStateShowRetryButton() {
        composeTestRule.setContent {
            MyNextFavoriteGameTheme {
                HomeScreen(
                    uiState = HomeUiState.Error("Network error"),
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onFavoriteClick = {},
                    onGameClick = {},
                    onRetry = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun errorStateClickRetry(){
        var retryCalled = false
        composeTestRule.setContent {
            MyNextFavoriteGameTheme {
                HomeScreen(
                    uiState = HomeUiState.Error("Network error"),
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onFavoriteClick = {},
                    onGameClick = {},
                    onRetry = {retryCalled = true}
                )
            }
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(retryCalled)
    }
}