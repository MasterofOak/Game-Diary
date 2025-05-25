package com.example.gamediary.model

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "TextRecords",
    foreignKeys = [
        ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["game_id"],
            onDelete = CASCADE
        ),
    ],
    indices = [Index("game_id")]
)
data class TextRecord(
    @PrimaryKey(autoGenerate = true) val textRId: Int = 0,
    @ColumnInfo(name = "game_id") val gameId: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "style_ranges") val styleRanges: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "last_edited_at") val lastEditedAt: Long? = null
)
