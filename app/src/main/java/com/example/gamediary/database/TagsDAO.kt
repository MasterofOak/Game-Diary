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
    
    @Query(
        "SELECT T.id, T.tag_name " +
                "FROM Tags T " +
                "JOIN GamesTags GT ON T.id = GT.tag_id " +
                "WHERE GT.game_id = :gameId"
    )
    fun getTagsByGameId(gameId: Int): Flow<List<Tag>>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGamesTags(gamesTags: GamesTags)
    
}