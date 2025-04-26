package com.example.gamediary.database

import com.example.gamediary.model.Game
import com.example.gamediary.model.Tag
import kotlinx.coroutines.flow.Flow

class OfflineGamesRepository(private val gamesDAO: GamesDAO, private val tagsDAO: TagsDAO) : GamesDBRepository {
    
    override fun getAllGames(): Flow<List<Game>> = gamesDAO.getAllGames()
    
    override fun getAllTags(): Flow<List<Tag>> = tagsDAO.getAllTags()
    
    override suspend fun insertGame(game: Game) = gamesDAO.insertGame(game)
    
    override suspend fun deleteGame(gameId: Int) = gamesDAO.deleteGame(gameId)
    
}