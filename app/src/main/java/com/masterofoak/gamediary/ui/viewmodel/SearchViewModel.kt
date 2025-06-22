package com.masterofoak.gamediary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterofoak.gamediary.database.GamesDBRepository
import com.masterofoak.gamediary.model.db_entities.Game
import com.masterofoak.gamediary.model.db_entities.ImageRecord
import com.masterofoak.gamediary.model.db_entities.Tag
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.model.db_entities.VideoRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(private val gamesDBRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    fun updateSearchQuery(text: String) {
        _uiState.update { it.copy(searchQuery = text) }
    }
    
    fun getSearchResults(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchResults = gamesDBRepository.getSearchResults(query)) }
        }
    }
    
}

sealed class SearchResult {
    data class GameResult(val game: Game) : SearchResult()
    data class TextRecordResult(val record: TextRecord) : SearchResult()
    data class ImageRecordResult(val record: ImageRecord) : SearchResult()
    data class VideoRecordResult(val record: VideoRecord) : SearchResult()
    data class TagResult(val tag: Tag) : SearchResult()
}

data class SearchUiState(
    var searchQuery: String = "",
    var searchResults: List<SearchResult> = emptyList<SearchResult>()
)

