package com.example.gamediary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gamediary.model.GamesTags
import com.example.gamediary.model.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDAO {
    
    @Query("SELECT * FROM Tags")
    fun getAllTags(): Flow<List<Tag>>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGamesTags(gamesTags: GamesTags)
}