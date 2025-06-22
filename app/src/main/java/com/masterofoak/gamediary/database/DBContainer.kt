package com.masterofoak.gamediary.database

import android.content.Context

interface DatabaseContainer {
    
    val gamesDBRepository: GamesDBRepository
}

class DBContainer(private val context: Context) : DatabaseContainer {
    
    override val gamesDBRepository: GamesDBRepository by lazy {
        val database = GameDiaryDatabase.getDatabase(context)
        OfflineGamesRepository(
            database.gamesDao(),
            database.tagsDao(),
            database.userRecordsDao(),
            database.searchFtsDao()
        )
    }
}