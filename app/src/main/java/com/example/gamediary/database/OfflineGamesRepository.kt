package com.example.gamediary.database

import com.example.gamediary.model.Game
import com.example.gamediary.model.GamesTags
import com.example.gamediary.model.Tag
import kotlinx.coroutines.flow.Flow

class OfflineGamesRepository(private val gamesDAO: GamesDAO, private val tagsDAO: TagsDAO) : GamesDBRepository {
    
    override fun getAllGames(): Flow<List<Game>> = gamesDAO.getAllGames()
    
    override suspend fun getGameById(gameId: Int): Game = gamesDAO.getGameById(gameId)
    
    override suspend fun insertGame(game: Game): Long = gamesDAO.insertGame(game)
    
    override suspend fun deleteGame(gameId: Int) = gamesDAO.deleteGame(gameId)
    
    override fun getAllTags(): Flow<List<Tag>> = tagsDAO.getAllTags()
    
    override fun getTagsByGameId(gameId: Int): Flow<List<Tag>> = tagsDAO.getTagsByGameId(gameId)
    
    override suspend fun insertTag(tag: Tag) = tagsDAO.insertTag(tag)
    
    override suspend fun insertGamesTags(gamesTags: GamesTags) = tagsDAO.insertGamesTags(gamesTags)
    
}