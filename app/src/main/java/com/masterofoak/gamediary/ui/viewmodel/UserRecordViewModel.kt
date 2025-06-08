package com.masterofoak.gamediary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterofoak.gamediary.database.GamesDBRepository
import com.masterofoak.gamediary.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserRecordViewModel(private val databaseRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserRecordUiState())
    val uiState: StateFlow<UserRecordUiState> = _uiState.asStateFlow()
    
    fun resetUiState() {
        _uiState.value = UserRecordUiState()
    }
    
    fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>> = databaseRepository.getAllTextRecords(gameId)
    
    fun getAllImageRecords(gameId: Int): Flow<List<ImageRecord>> = databaseRepository.getAllImageRecords(gameId)
    
    fun getAllVideoRecords(gameId: Int): Flow<List<VideoRecord>> = databaseRepository.getAllVideoRecords(gameId)
    
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
    
    fun setSelectedGame(gameId: Int?, gameImage: String?) {
        _uiState.update { it.copy(currentlySelectedGameId = gameId, currentlySelectedGameImage = gameImage) }
    }
    
    fun updateTextRecordState(textContent: String, styleRange: List<StyleRange>) {
        _uiState.update { it.copy(textContent = textContent, styleRanges = styleRange) }
    }
    
    fun updateImageRecordState(imageUri: String) {
        _uiState.update { it.copy(imageUri = imageUri) }
    }
    
    fun updateVideoRecordState(videoUri: String) {
        _uiState.update { it.copy(videoUri = videoUri) }
    }
    
    fun getAllGamesList(): Flow<List<Game>> = databaseRepository.getAllGames()
}

data class UserRecordUiState(
    var currentlySelectedGameId: Int? = null,
    var currentlySelectedGameImage: String? = null,
    var textContent: String? = null,
    var styleRanges: List<StyleRange>? = null,
    var imageUri: String? = null,
    var videoUri: String? = null
)

