package com.example.gamediary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamediary.data.FakeGamesData
import com.example.gamediary.database.GamesDBRepository
import com.example.gamediary.model.Game
import com.example.gamediary.model.Tag
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GamesViewModel(private val databaseRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GamesUiState(isLoading = true))
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            databaseRepository.getAllGames().collectLatest { games ->
                if (!games.isEmpty()) {
                    _uiState.value = GamesUiState(gamesList = games)
                } else {
                    _uiState.value = GamesUiState(gamesList = FakeGamesData.games)
                }
            }
        }
    }
    
    fun setGame(gameId: Int) {
        _uiState.update { it.copy(currentGame = it.gamesList.find { it.id == gameId }) }
    }
    
    /**
    THINK: Is it worth to fetch information from DB or just use function above
     */
    fun setGameViaDb(gameId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(2000L)
            _uiState.update { it.copy(currentGame = databaseRepository.getGameById(gameId), isLoading = false) }
        }
    }
    
    fun deleteGame(gameId: Int) {
        viewModelScope.launch {
            databaseRepository.deleteGame(gameId)
        }
    }
    
    fun getGamesTags(gameId: Int) = databaseRepository.getTagsByGameId(gameId)
    
}

data class GamesUiState(
    var gamesList: List<Game> = emptyList(),
    var currentGame: Game? = null,
    var isLoading: Boolean = false
)

data class GameWithTags(
    val id: Int,
    val gameName: String,
    val imageUri: String? = null,
    val tags: List<Tag> = emptyList()
)

fun Game.toGameWithTags(tagsList: List<Tag>): GameWithTags = GameWithTags(
    id = id,
    gameName = gameName,
    imageUri = imageUri,
    tags = tagsList
)