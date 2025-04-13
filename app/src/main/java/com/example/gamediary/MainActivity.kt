package com.example.gamediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.gamediary.ui.screens.GameDiaryApp
import com.example.gamediary.ui.theme.GameDiaryTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameDiaryTheme {
                val gameDiaryNavController = rememberNavController()
                GameDiaryApp(navController = gameDiaryNavController)
            }
        }
    }
}