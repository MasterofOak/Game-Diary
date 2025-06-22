package com.masterofoak.gamediary.database

import com.masterofoak.gamediary.model.Records
import com.masterofoak.gamediary.model.db_entities.*
import com.masterofoak.gamediary.ui.viewmodel.SearchResult
import kotlinx.coroutines.flow.Flow

interface GamesDBRepository {
    
    /**
     *  [Game] related database operations
     */
    fun getAllGames(): Flow<List<Game>>
    suspend fun getGameById(gameId: Int): Game
    suspend fun insertGame(game: Game): Long
    suspend fun deleteGame(gameId: Int)
    
    /**
     * [Tag] related database operations
     */
    fun getAllTags(): Flow<List<Tag>>
    fun getTagsByGameId(gameId: Int): Flow<List<Tag>>
    suspend fun insertTag(tag: Tag)
    suspend fun insertGamesTags(gamesTags: GamesTags)
    
    /**
     * [Records] related database operations
     */
    fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>>
    fun getAllImageRecords(gameId: Int): Flow<List<ImageRecord>>
    fun getAllVideoRecords(gameId: Int): Flow<List<VideoRecord>>
    
    suspend fun addCaptionToImage(id: Int, caption: String)
    suspend fun addCaptionToVideo(id: Int, caption: String)
    
    suspend fun updateTextRecord(textRecord: TextRecord)
    suspend fun updateImageRecord(imageRecord: ImageRecord)
    suspend fun updateVideoRecord(videoRecord: VideoRecord)
    
    suspend fun insertTextRecord(textRecord: TextRecord)
    suspend fun insertImageRecord(imageRecord: ImageRecord)
    suspend fun insertVideoRecord(videoRecord: VideoRecord)
    
    suspend fun deleteTextRecord(textRecord: TextRecord)
    suspend fun deleteImageRecord(imageRecord: ImageRecord)
    suspend fun deleteVideoRecord(videoRecord: VideoRecord)
    
    /**
     * [SearchResult] related database operations
     */
    
    suspend fun getGamesSearchResults(query: String): List<Game>
    suspend fun getTextRecordsSearchResults(query: String): List<TextRecord>
    suspend fun getImageRecordsSearchResults(query: String): List<ImageRecord>
    suspend fun getVideoRecordsSearchResults(query: String): List<VideoRecord>
    suspend fun getTagsSearchResults(query: String): List<Tag>
    suspend fun getSearchResults(query: String): List<SearchResult>
    
}