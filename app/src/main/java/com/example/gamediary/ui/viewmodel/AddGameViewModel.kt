package com.example.gamediary.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamediary.data.tagsStyling
import com.example.gamediary.database.GamesDBRepository
import com.example.gamediary.model.Game
import com.example.gamediary.model.Tag
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddGameViewModel(private val databaseRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddGameUiState())
    val uiState: StateFlow<AddGameUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            databaseRepository.getAllTags().collectLatest { tags ->
                val tagsList: List<FullTag> = tags.map { tag -> tag.toFullTag() }
                _uiState.value = AddGameUiState(tagsList = tagsList)
                println(tagsList)
            }
        }
    }
    
    fun addGame(gameName: String, imageUrl: String?) {
        viewModelScope.launch {
            databaseRepository.insertGame(game = Game(gameName = gameName, imageUri = imageUrl))
        }
    }
    
    fun addSelectedTagToList(tagId: Int) {
        val selectedTags = _uiState.value.selectedTags.toMutableList()
        if (selectedTags.contains(tagId)) {
            selectedTags.remove(tagId)
        } else {
            selectedTags.add(tagId)
        }
        _uiState.update { it.copy(selectedTags = selectedTags.toList()) }
    }
    
    fun addCustomTag(tagName: String) = viewModelScope.launch {
        databaseRepository.insertTag(tag = Tag(tagName = tagName))
    }
    
    fun clearSelectedTags() {
        _uiState.update { it.copy(selectedTags = emptyList()) }
    }
    
}

data class AddGameUiState(
    var tagsList: List<FullTag> = emptyList(),
    var selectedTags: List<Int> = emptyList()
)

data class FullTag(
    val tagId: Int,
    val tagName: String,
    val containerColor: Color,
    val outlineColor: Color,
    val tagIcon: ImageVector? = null
)

fun Tag.toFullTag(): FullTag = FullTag(
    tagId = id,
    tagName = tagName,
    containerColor = tagsStyling[id - 1].containerColor,
    outlineColor = tagsStyling[id - 1].outlineColor,
    tagIcon = tagsStyling[id - 1].tagIcon,
)