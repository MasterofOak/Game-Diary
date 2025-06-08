package com.masterofoak.gamediary.model

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.masterofoak.gamediary.ui.screens.Records

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
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "textRId") override val id: Int = 0,
    @ColumnInfo(name = "game_id") val gameId: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "style_ranges") val styleRanges: String,
    @ColumnInfo(name = "created_at") override val createdAt: Long,
    @ColumnInfo(name = "last_edited_at") val lastEditedAt: Long? = null,
) : Records {
    
    @Ignore
    override val recordType: RecordType = RecordType.TEXT
}
