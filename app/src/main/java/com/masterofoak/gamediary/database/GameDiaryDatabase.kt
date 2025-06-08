package com.masterofoak.gamediary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.masterofoak.gamediary.model.*

@Database(
    version = GameDiaryDatabase.LATEST_VERSION,
    entities = [Game::class, Tag::class, GamesTags::class, TextRecord::class, ImageRecord::class, VideoRecord::class],
    exportSchema = true
)
abstract class GameDiaryDatabase : RoomDatabase() {
    
    abstract fun gamesDao(): GamesDAO
    abstract fun tagsDao(): TagsDAO
    abstract fun userRecordsDao(): UserRecordsDAO
    
    companion object {
        
        const val LATEST_VERSION = 7
        
        @Volatile
        private var Instance: GameDiaryDatabase? = null
        
        fun getDatabase(context: Context): GameDiaryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDiaryDatabase::class.java, "game_diary")
                    .createFromAsset("database/game_diary.db")
                    .build().also { Instance = it }
            }
        }
        
    }
}