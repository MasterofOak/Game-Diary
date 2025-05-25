package com.example.gamediary.database

import androidx.room.*
import com.example.gamediary.model.ImageRecord
import com.example.gamediary.model.TextRecord
import com.example.gamediary.model.VideoRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecordsDAO {
    
    @Query("SELECT * FROM TextRecords TX WHERE TX.game_id = :gameId")
    fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTextRecord(textRecord: TextRecord)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImageRecord(imageRecord: ImageRecord)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVideoRecord(videoRecord: VideoRecord)
    
    @Delete
    suspend fun deleteTextRecord(textRecord: TextRecord)
    
    @Delete
    suspend fun deleteImageRecord(imageRecord: ImageRecord)
    
    @Delete
    suspend fun deleteVideoRecord(videoRecord: VideoRecord)
    
}