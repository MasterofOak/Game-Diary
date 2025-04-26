package com.example.gamediary.database

import com.example.gamediary.model.Game
import com.example.gamediary.model.Tag
import kotlinx.coroutines.flow.Flow

interface GamesDBRepository {
    
    fun getAllGames(): Flow<List<Game>>
    fun getAllTags(): Flow<List<Tag>>
    suspend fun insertGame(game: Game)
    suspend fun deleteGame(gameId: Int)
}