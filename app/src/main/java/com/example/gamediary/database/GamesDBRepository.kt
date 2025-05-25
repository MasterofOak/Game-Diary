package com.example.gamediary.database

import com.example.gamediary.model.*
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
    
    suspend fun insertTextRecord(textRecord: TextRecord)
    suspend fun insertImageRecord(imageRecord: ImageRecord)
    suspend fun insertVideoRecord(videoRecord: VideoRecord)
    
    suspend fun deleteTextRecord(textRecord: TextRecord)
    suspend fun deleteImageRecord(imageRecord: ImageRecord)
    suspend fun deleteVideoRecord(videoRecord: VideoRecord)
}