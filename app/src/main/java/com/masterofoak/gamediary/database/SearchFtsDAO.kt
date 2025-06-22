package com.masterofoak.gamediary.database

import androidx.room.Dao
import androidx.room.Query
import com.masterofoak.gamediary.model.db_entities.*

@Dao
interface SearchFtsDAO {
    
    @Query(
        """
            SELECT g.*
            FROM Games g
            JOIN Games_fts g_fts ON g_fts.rowid = g.rowid
            WHERE Games_fts MATCH :query
        """
    )
    suspend fun searchGames(query: String): List<Game>
    
    @Query(
        """
            SELECT tr.*
            FROM TextRecords tr
            JOIN text_records_fts tr_fts ON tr_fts.rowid = tr.rowid
            WHERE text_records_fts MATCH :query
        """
    )
    suspend fun searchTextRecords(query: String): List<TextRecord>
    
    @Query(
        """
            SELECT ir.*
            FROM ImageRecords ir
            JOIN image_records_fts ir_fts ON ir_fts.rowid = ir.rowid
            WHERE image_records_fts MATCH :query
        """
    )
    suspend fun searchImageRecords(query: String): List<ImageRecord>
    
    @Query(
        """
            SELECT vr.*
            FROM VideoRecords vr
            JOIN video_records_fts vr_fts ON vr_fts.rowid = vr.rowid
            WHERE video_records_fts MATCH :query
        """
    )
    suspend fun searchVideoRecords(query: String): List<VideoRecord>
    
    @Query(
        """
            SELECT t.*
            FROM Tags t
            JOIN tags_fts t_fts ON t_fts.rowid = t.rowid
            WHERE tags_fts MATCH :query
        """
    )
    suspend fun searchTags(query: String): List<Tag>
}