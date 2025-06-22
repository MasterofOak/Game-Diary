package com.masterofoak.gamediary.ui.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterofoak.gamediary.FILEPROVIDER_AUTHORITY
import com.masterofoak.gamediary.database.GamesDBRepository
import com.masterofoak.gamediary.model.RecordType
import com.masterofoak.gamediary.model.Records
import com.masterofoak.gamediary.model.StyleRange
import com.masterofoak.gamediary.model.db_entities.Game
import com.masterofoak.gamediary.model.db_entities.ImageRecord
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.model.db_entities.VideoRecord
import com.masterofoak.gamediary.utils.deleteFileFromUri
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class UserRecordViewModel(private val gamesDBRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserRecordUiState())
    val uiState: StateFlow<UserRecordUiState> = _uiState.asStateFlow()
    
    fun resetUiState() {
        _uiState.value = UserRecordUiState()
    }
    
    fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>> = gamesDBRepository.getAllTextRecords(gameId)
    fun getAllImageRecords(gameId: Int): Flow<List<ImageRecord>> = gamesDBRepository.getAllImageRecords(gameId)
    fun getAllVideoRecords(gameId: Int): Flow<List<VideoRecord>> = gamesDBRepository.getAllVideoRecords(gameId)
    fun addTextRecords(textRecord: TextRecord) {
        viewModelScope.launch {
            gamesDBRepository.insertTextRecord(textRecord)
        }
    }
    
    fun addImageRecords(imageRecord: ImageRecord) {
        viewModelScope.launch {
            gamesDBRepository.insertImageRecord(imageRecord)
        }
    }
    
    fun addVideoRecords(videoRecord: VideoRecord) {
        viewModelScope.launch {
            gamesDBRepository.insertVideoRecord(videoRecord)
        }
    }
    
    fun addCaptionToImage(id: Int, caption: String) {
        viewModelScope.launch {
            gamesDBRepository.addCaptionToImage(id, caption)
        }
    }
    
    fun addCaptionToVideo(id: Int, caption: String) {
        viewModelScope.launch {
            gamesDBRepository.addCaptionToVideo(id, caption)
        }
    }
    
    fun deleteRecord(record: Records, recordType: RecordType, filesDir: File) {
        viewModelScope.launch {
            when (recordType) {
                RecordType.TEXT -> gamesDBRepository.deleteTextRecord(record as TextRecord)
                
                RecordType.IMAGE -> {
                    gamesDBRepository.deleteImageRecord(record as ImageRecord)
                    val fileUri = record.imageUri.toUri()
                    if (fileUri.authority == FILEPROVIDER_AUTHORITY) deleteFileFromUri(fileUri, filesDir, "images/")
                }
                
                RecordType.VIDEO -> {
                    gamesDBRepository.deleteVideoRecord(record as VideoRecord)
                    val fileUri = record.videoUri.toUri()
                    if (fileUri.authority == FILEPROVIDER_AUTHORITY) deleteFileFromUri(fileUri, filesDir, "videos/")
                }
            }
        }
    }
    
    fun updateTextRecord(textRecord: TextRecord) {
        viewModelScope.launch {
            gamesDBRepository.updateTextRecord(textRecord)
        }
    }
    
    fun setSelectedGame(gameId: Int?, gameImage: String?, gameName: String?) {
        _uiState.update {
            it.copy(
                currentlySelectedGameId = gameId,
                currentlySelectedGameImage = gameImage,
                currentlySelectedGameName = gameName
            )
        }
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
    
    fun getAllGamesList(): Flow<List<Game>> = gamesDBRepository.getAllGames()
}

data class UserRecordUiState(
    var currentlySelectedGameId: Int? = null,
    var currentlySelectedGameName: String? = null,
    var currentlySelectedGameImage: String? = null,
    var textContent: String? = null,
    var styleRanges: List<StyleRange>? = null,
    var imageUri: String? = null,
    var videoUri: String? = null
)

