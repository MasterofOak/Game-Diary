package com.example.gamediary.database

import com.example.gamediary.model.*
import kotlinx.coroutines.flow.Flow

class OfflineGamesRepository(
    private val gamesDAO: GamesDAO,
    private val tagsDAO: TagsDAO,
    private val userRecordsDAO: UserRecordsDAO
) : GamesDBRepository {
    
    /**
     *  [Game] related database operations
     */
    
    override fun getAllGames(): Flow<List<Game>> = gamesDAO.getAllGames()
    
    override suspend fun getGameById(gameId: Int): Game = gamesDAO.getGameById(gameId)
    
    override suspend fun insertGame(game: Game): Long = gamesDAO.insertGame(game)
    
    override suspend fun deleteGame(gameId: Int) = gamesDAO.deleteGame(gameId)
    
    /**
     * [Tag] related database operations
     */
    
    override fun getAllTags(): Flow<List<Tag>> = tagsDAO.getAllTags()
    
    override fun getTagsByGameId(gameId: Int): Flow<List<Tag>> = tagsDAO.getTagsByGameId(gameId)
    
    override suspend fun insertTag(tag: Tag) = tagsDAO.insertTag(tag)
    
    override suspend fun insertGamesTags(gamesTags: GamesTags) = tagsDAO.insertGamesTags(gamesTags)
    
    /**
     * [Records] related database operations
     */
    
    override fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>> = userRecordsDAO.getAllTextRecords(gameId)
    
    override suspend fun insertTextRecord(textRecord: TextRecord) = userRecordsDAO.insertTextRecord(textRecord)
    
    override suspend fun insertImageRecord(imageRecord: ImageRecord) = userRecordsDAO.insertImageRecord(imageRecord)
    
    override suspend fun insertVideoRecord(videoRecord: VideoRecord) = userRecordsDAO.insertVideoRecord(videoRecord)
    
    override suspend fun deleteTextRecord(textRecord: TextRecord) = userRecordsDAO.deleteTextRecord(textRecord)
    
    override suspend fun deleteImageRecord(imageRecord: ImageRecord) = userRecordsDAO.deleteImageRecord(imageRecord)
    
    override suspend fun deleteVideoRecord(videoRecord: VideoRecord) = userRecordsDAO.deleteVideoRecord(videoRecord)
    
}