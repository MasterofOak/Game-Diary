package com.example.gamediary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamediary.database.GamesDBRepository
import com.example.gamediary.model.Game
import com.example.gamediary.model.ImageRecord
import com.example.gamediary.model.TextRecord
import com.example.gamediary.model.VideoRecord
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserRecordViewModel(private val databaseRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserRecordUiState())
    val uiState: StateFlow<UserRecordUiState> = _uiState.asStateFlow()
    
    fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>> = databaseRepository.getAllTextRecords(gameId)
    
    fun addTextRecords(textRecord: TextRecord) {
        viewModelScope.launch {
            databaseRepository.insertTextRecord(textRecord)
        }
    }
    
    fun addImageRecords(imageRecord: ImageRecord) {
        viewModelScope.launch {
            databaseRepository.insertImageRecord(imageRecord)
        }
    }
    
    fun addVideoRecords(videoRecord: VideoRecord) {
        viewModelScope.launch {
            databaseRepository.insertVideoRecord(videoRecord)
        }
    }
    
    fun setCurrentGame(gameId: Int?, gameImage: String?) {
        _uiState.update { it.copy(currentlySelectedGameId = gameId, currentlySelectedGameImage = gameImage) }
    }
    
    fun getAllGamesList(): Flow<List<Game>> = databaseRepository.getAllGames()
}

data class UserRecordUiState(var currentlySelectedGameId: Int? = null, var currentlySelectedGameImage: String? = null)

