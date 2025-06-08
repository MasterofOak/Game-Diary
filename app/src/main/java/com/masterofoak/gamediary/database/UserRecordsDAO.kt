package com.masterofoak.gamediary.database

import androidx.room.*
import com.masterofoak.gamediary.model.ImageRecord
import com.masterofoak.gamediary.model.TextRecord
import com.masterofoak.gamediary.model.VideoRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecordsDAO {
    
    @Query("SELECT * FROM TextRecords TX WHERE TX.game_id = :gameId")
    fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>>
    
    @Query("SELECT * FROM ImageRecords IR WHERE IR.game_id = :gameId")
    fun getAllImageRecords(gameId: Int): Flow<List<ImageRecord>>
    
    @Query("SELECT * FROM VideoRecords VR WHERE VR.game_id = :gameId")
    fun getAllVideoRecords(gameId: Int): Flow<List<VideoRecord>>
    
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