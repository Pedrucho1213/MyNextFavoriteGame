package com.example.mynextfavoritegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mynextfavoritegame.presentation.main.MainScreen
import com.example.mynextfavoritegame.ui.theme.MyNextFavoriteGameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyNextFavoriteGameTheme {
                MainScreen()
            }
        }
    }
}