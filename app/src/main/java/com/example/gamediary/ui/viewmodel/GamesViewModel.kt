package com.example.gamediary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gamediary.GameDiaryApplication
import com.example.gamediary.database.GamesDBRepository
import com.example.gamediary.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GamesViewModel(private val databaseRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GamesUiState(isLoading = true))
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            databaseRepository.getAllGames().collectLatest { games ->
                _uiState.value = GamesUiState(gamesList = games)
            }
        }
    }
    
    fun addGame(gameName: String, imageUrl: String?) {
        viewModelScope.launch {
            databaseRepository.insertGame(game = Game(gameName = gameName, imageUri = imageUrl))
        }
    }
    
    fun deleteGame(gameId: Int) {
        viewModelScope.launch {
            databaseRepository.deleteGame(gameId)
        }
    }
    
    
    companion object {
        
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameDiaryApplication)
                GamesViewModel(application.container.gamesDBRepository)
            }
        }
    }
}

data class GamesUiState(
    var gamesList: List<Game> = emptyList(),
    var isLoading: Boolean = false
)