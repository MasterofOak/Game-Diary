package com.masterofoak.gamediary.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterofoak.gamediary.data.tagsStyling
import com.masterofoak.gamediary.database.GamesDBRepository
import com.masterofoak.gamediary.model.db_entities.Game
import com.masterofoak.gamediary.model.db_entities.GamesTags
import com.masterofoak.gamediary.model.db_entities.Tag
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AddGameUiState(
    var tagsList: List<FullTag> = emptyList(),
    var selectedTags: List<Int> = emptyList(),
    var gameToUpdate: Game? = null
)

class AddGameViewModel(private val databaseRepository: GamesDBRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddGameUiState())
    val uiState: StateFlow<AddGameUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            databaseRepository.getAllTags().collectLatest { tags ->
                val tagsList: List<FullTag> = tags.map { tag -> tag.toFullTag() }
                _uiState.value = AddGameUiState(tagsList = tagsList)
            }
        }
    }
    
    fun addGame(gameName: String, imageUrl: String?) {
        val tags = _uiState.value.selectedTags
        viewModelScope.launch {
            val gameId: Long = databaseRepository.insertGame(game = Game(gameName = gameName, imageUri = imageUrl))
            for (tag in tags) {
                databaseRepository.insertGamesTags(GamesTags(gameId = gameId.toInt(), tagId = tag))
            }
        }
    }
    
    fun addCustomTag(tagName: String) = viewModelScope.launch {
        databaseRepository.insertTag(tag = Tag(tagName = tagName, isCustomTag = true))
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
    
    fun clearSelectedTags() {
        _uiState.update { it.copy(selectedTags = emptyList()) }
    }
}

data class FullTag(
    val tagId: Int,
    val tagName: String,
    val containerColor: Color,
    val outlineColor: Color,
)

fun Tag.toFullTag(): FullTag {
    return if (!isCustomTag) {
        FullTag(
            tagId = id,
            tagName = tagName,
            containerColor = tagsStyling[id - 1].containerColor,
            outlineColor = tagsStyling[id - 1].outlineColor,
        )
    } else {
        FullTag(
            tagId = id,
            tagName = tagName,
            containerColor = Color(0xFF7AFA69),
            outlineColor = Color(0xFF69FABE),
        )
    }
}