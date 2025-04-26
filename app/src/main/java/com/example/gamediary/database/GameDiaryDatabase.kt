package com.example.gamediary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamediary.model.Game
import com.example.gamediary.model.Tag

@Database(version = GameDiaryDatabase.LATEST_VERSION, entities = [Game::class, Tag::class], exportSchema = true)
abstract class GameDiaryDatabase : RoomDatabase() {
    
    abstract fun gamesDao(): GamesDAO
    abstract fun tagsDao(): TagsDAO
    
    companion object {
        
        const val LATEST_VERSION = 1
        
        @Volatile
        private var Instance: GameDiaryDatabase? = null
        
        fun getDatabase(context: Context): GameDiaryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDiaryDatabase::class.java, "game_diary")
                    .createFromAsset("database/game_diary.db").build().also { Instance = it }
            }
        }
        
    }
}