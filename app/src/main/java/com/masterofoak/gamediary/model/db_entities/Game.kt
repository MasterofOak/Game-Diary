package com.masterofoak.gamediary.model.db_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Games")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "game_name") val gameName: String,
    @ColumnInfo(name = "image_uri") val imageUri: String? = null
)