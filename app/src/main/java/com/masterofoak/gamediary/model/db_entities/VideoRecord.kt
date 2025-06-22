package com.masterofoak.gamediary.model.db_entities

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.masterofoak.gamediary.model.RecordType
import com.masterofoak.gamediary.model.Records

@Entity(
    tableName = "VideoRecords",
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
data class VideoRecord(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "videoRId") override val id: Int = 0,
    @ColumnInfo(name = "game_id") val gameId: Int,
    @ColumnInfo(name = "video_uri") val videoUri: String,
    @ColumnInfo(name = "caption") val caption: String? = null,
    @ColumnInfo(name = "created_at") override val createdAt: Long,
    @ColumnInfo(name = "last_edited_at") val lastEditedAt: Long? = null
) : Records {
    
    @Ignore
    override val recordType: RecordType = RecordType.VIDEO
    
}
