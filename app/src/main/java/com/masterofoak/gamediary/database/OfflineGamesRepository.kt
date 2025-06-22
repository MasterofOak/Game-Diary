package com.masterofoak.gamediary.database

import com.masterofoak.gamediary.model.Records
import com.masterofoak.gamediary.model.db_entities.*
import com.masterofoak.gamediary.ui.viewmodel.SearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow


class OfflineGamesRepository(
    private val gamesDAO: GamesDAO,
    private val tagsDAO: TagsDAO,
    private val userRecordsDAO: UserRecordsDAO,
    private val searchFtsDAO: SearchFtsDAO
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
    
    override fun getAllImageRecords(gameId: Int): Flow<List<ImageRecord>> = userRecordsDAO.getAllImageRecords(gameId)
    
    override fun getAllVideoRecords(gameId: Int): Flow<List<VideoRecord>> = userRecordsDAO.getAllVideoRecords(gameId)
    
    override suspend fun addCaptionToImage(id: Int, caption: String) = userRecordsDAO.addCaptionToImage(id, caption)
    
    override suspend fun addCaptionToVideo(id: Int, caption: String) = userRecordsDAO.addCaptionToVideo(id, caption)
    
    override suspend fun insertTextRecord(textRecord: TextRecord) = userRecordsDAO.insertTextRecord(textRecord)
    
    override suspend fun insertImageRecord(imageRecord: ImageRecord) = userRecordsDAO.insertImageRecord(imageRecord)
    
    override suspend fun insertVideoRecord(videoRecord: VideoRecord) = userRecordsDAO.insertVideoRecord(videoRecord)
    
    override suspend fun deleteTextRecord(textRecord: TextRecord) = userRecordsDAO.deleteTextRecord(textRecord)
    
    override suspend fun deleteImageRecord(imageRecord: ImageRecord) = userRecordsDAO.deleteImageRecord(imageRecord)
    
    override suspend fun deleteVideoRecord(videoRecord: VideoRecord) = userRecordsDAO.deleteVideoRecord(videoRecord)
    
    override suspend fun updateTextRecord(textRecord: TextRecord) = userRecordsDAO.updateTextRecord(textRecord)
    
    override suspend fun updateImageRecord(imageRecord: ImageRecord) = userRecordsDAO.updateImageRecord(imageRecord)
    
    override suspend fun updateVideoRecord(videoRecord: VideoRecord) = userRecordsDAO.updateVideoRecord(videoRecord)
    
    /**
     * [SearchResult] related database operations
     */
    override suspend fun getGamesSearchResults(query: String): List<Game> = searchFtsDAO.searchGames(query)
    
    override suspend fun getTextRecordsSearchResults(query: String): List<TextRecord> = searchFtsDAO
        .searchTextRecords(query)
    
    override suspend fun getImageRecordsSearchResults(query: String): List<ImageRecord> = searchFtsDAO
        .searchImageRecords(query)
    
    override suspend fun getVideoRecordsSearchResults(query: String): List<VideoRecord> = searchFtsDAO
        .searchVideoRecords(query)
    
    override suspend fun getTagsSearchResults(query: String): List<Tag> = searchFtsDAO.searchTags(query)
    
    override suspend fun getSearchResults(query: String): List<SearchResult> = coroutineScope {
        val gamesResults = async { getGamesSearchResults(query) }
        val textRecordsResults = async { getTextRecordsSearchResults(query) }
        val imageRecordsResults = async { getImageRecordsSearchResults(query) }
        val videoRecordsResults = async { getVideoRecordsSearchResults(query) }
        val tagsResults = async { getTagsSearchResults(query) }
        return@coroutineScope buildList {
            addAll(gamesResults.await().map { SearchResult.GameResult(it) })
            addAll(textRecordsResults.await().map { SearchResult.TextRecordResult(it) })
            addAll(imageRecordsResults.await().map { SearchResult.ImageRecordResult(it) })
            addAll(videoRecordsResults.await().map { SearchResult.VideoRecordResult(it) })
            addAll(tagsResults.await().map { SearchResult.TagResult(it) })
        }
    }
    
}