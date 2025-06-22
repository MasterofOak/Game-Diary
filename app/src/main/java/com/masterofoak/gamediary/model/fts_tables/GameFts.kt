package com.masterofoak.gamediary.model.fts_tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.masterofoak.gamediary.model.db_entities.Game

@Entity(tableName = "Games_fts")
@Fts4(contentEntity = Game::class)
data class GameFts(
    @ColumnInfo(name = "rowid") @PrimaryKey val id: Int,
    @ColumnInfo(name = "game_name") val gameName: String
)