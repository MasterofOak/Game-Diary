package com.example.gamediary.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.gamediary.data.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GamesViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()
    
    fun addGame(gameName: String, imageUrl: Uri?) {
        _uiState.update { state ->
            val currentList = state.gamesList
            currentList.add(Game(gameName, imageUrl))
            state.copy(gamesList = currentList)
        }
    }
}

data class GamesUiState(
    var gamesList: MutableList<Game> = mutableListOf()
)