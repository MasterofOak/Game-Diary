package com.example.gamediary.model

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "ImageRecords",
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
data class ImageRecord(
    @PrimaryKey(autoGenerate = true) val imageRId: Int = 0,
    @ColumnInfo(name = "game_id") val gameId: Int,
    @ColumnInfo(name = "image_uri") val imageUri: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "last_edited_at") val lastEditedAt: Long? = null
)
