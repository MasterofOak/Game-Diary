package com.example.gamediary.database

import com.example.gamediary.model.Game
import com.example.gamediary.model.GamesTags
import com.example.gamediary.model.Tag
import kotlinx.coroutines.flow.Flow

interface GamesDBRepository {
    
    /**
     *  [Game] related database functions
     */
    fun getAllGames(): Flow<List<Game>>
    suspend fun getGameById(gameId: Int): Game
    suspend fun insertGame(game: Game): Long
    suspend fun deleteGame(gameId: Int)
    
    /**
     * [Tag] related database functions
     */
    fun getAllTags(): Flow<List<Tag>>
    suspend fun insertTag(tag: Tag)
    suspend fun insertGamesTags(gamesTags: GamesTags)
}