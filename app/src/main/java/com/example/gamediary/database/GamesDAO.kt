package com.example.gamediary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gamediary.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDAO {
    
    @Query("SELECT * FROM Games")
    fun getAllGames(): Flow<List<Game>>
    
    @Query("SELECT * FROM Games WHERE id = :gameId")
    suspend fun getGameById(gameId: Int): Game
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGame(game: Game): Long
    
    @Query("DELETE FROM Games Where id = :gameId")
    suspend fun deleteGame(gameId: Int)
}