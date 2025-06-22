package com.masterofoak.gamediary.model.db_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "GamesTags",
    primaryKeys = ["game_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["game_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("game_id"), Index("tag_id")]
)
data class GamesTags(
    @ColumnInfo(name = "game_id") val gameId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int
)
