package com.masterofoak.gamediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.masterofoak.gamediary.ui.GameDiaryApp
import com.masterofoak.gamediary.ui.theme.GameDiaryTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameDiaryTheme {
                val gameDiaryNavController = rememberNavController()
                GameDiaryApp(
                    gameDiaryNavController = gameDiaryNavController
                )
            }
        }
    }
    
}
