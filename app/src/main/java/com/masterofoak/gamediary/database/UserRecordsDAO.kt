package com.masterofoak.gamediary.database

import androidx.room.*
import com.masterofoak.gamediary.model.db_entities.ImageRecord
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.model.db_entities.VideoRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecordsDAO {
    
    @Query("SELECT * FROM TextRecords TR WHERE TR.game_id = :gameId")
    fun getAllTextRecords(gameId: Int): Flow<List<TextRecord>>
    
    @Query("SELECT * FROM ImageRecords IR WHERE IR.game_id = :gameId")
    fun getAllImageRecords(gameId: Int): Flow<List<ImageRecord>>
    
    @Query("SELECT * FROM VideoRecords VR WHERE VR.game_id = :gameId")
    fun getAllVideoRecords(gameId: Int): Flow<List<VideoRecord>>
    
    @Query("UPDATE ImageRecords SET caption = :caption WHERE imageRId = :id")
    suspend fun addCaptionToImage(id: Int, caption: String)
    
    @Query("UPDATE VideoRecords SET caption = :caption WHERE videoRId = :id")
    suspend fun addCaptionToVideo(id: Int, caption: String)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTextRecord(textRecord: TextRecord)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageRecord(imageRecord: ImageRecord)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoRecord(videoRecord: VideoRecord)
    
    @Update
    suspend fun updateTextRecord(textRecord: TextRecord)
    
    @Update
    suspend fun updateImageRecord(imageRecord: ImageRecord)
    
    @Update
    suspend fun updateVideoRecord(videoRecord: VideoRecord)
    
    @Delete
    suspend fun deleteTextRecord(textRecord: TextRecord)
    
    @Delete
    suspend fun deleteImageRecord(imageRecord: ImageRecord)
    
    @Delete
    suspend fun deleteVideoRecord(videoRecord: VideoRecord)
    
}