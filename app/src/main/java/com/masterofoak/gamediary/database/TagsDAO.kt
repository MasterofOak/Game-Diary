package com.masterofoak.gamediary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.masterofoak.gamediary.model.db_entities.GamesTags
import com.masterofoak.gamediary.model.db_entities.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDAO {
    
    @Query("SELECT * FROM Tags")
    fun getAllTags(): Flow<List<Tag>>
    
    @Query(
        "SELECT T.id, T.tag_name, T.is_custom_tag " +
                "FROM Tags T " +
                "JOIN GamesTags GT ON T.id = GT.tag_id " +
                "WHERE GT.game_id = :gameId"
    )
    fun getTagsByGameId(gameId: Int): Flow<List<Tag>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGamesTags(gamesTags: GamesTags)
    
    @Query("INSERT INTO tags_fts(rowid, tag_name) SELECT id, tag_name FROM Tags")
    suspend fun insertTagsIntoFts()
}