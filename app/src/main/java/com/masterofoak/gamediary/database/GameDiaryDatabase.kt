package com.masterofoak.gamediary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.masterofoak.gamediary.model.db_entities.Game
import com.masterofoak.gamediary.model.db_entities.GamesTags
import com.masterofoak.gamediary.model.db_entities.ImageRecord
import com.masterofoak.gamediary.model.db_entities.Tag
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.model.db_entities.VideoRecord
import com.masterofoak.gamediary.model.fts_tables.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    version = GameDiaryDatabase.LATEST_VERSION,
    entities = [
        Game::class, Tag::class, GamesTags::class, TextRecord::class, ImageRecord::class, VideoRecord::class,
        GameFts::class, TextRecordFts::class, ImageRecordFts::class, VideoRecordFts::class, TagFts::class
    ],
    exportSchema = true
)
abstract class GameDiaryDatabase : RoomDatabase() {
    
    abstract fun gamesDao(): GamesDAO
    abstract fun tagsDao(): TagsDAO
    abstract fun userRecordsDao(): UserRecordsDAO
    abstract fun searchFtsDao(): SearchFtsDAO
    
    companion object {
        
        const val LATEST_VERSION = 11
        
        @Volatile
        private var Instance: GameDiaryDatabase? = null
        fun getDatabase(context: Context): GameDiaryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDiaryDatabase::class.java, "game_diary")
                    .createFromAsset("database/game_diary.db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val db = getDatabase(context)
                                db.tagsDao().insertTagsIntoFts()
                            }
                        }
                    })
                    .build().also { Instance = it }
            }
        }
    }
}