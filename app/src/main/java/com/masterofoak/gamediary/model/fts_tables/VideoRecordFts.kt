package com.masterofoak.gamediary.model.fts_tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.masterofoak.gamediary.model.db_entities.VideoRecord

@Entity(tableName = "video_records_fts")
@Fts4(contentEntity = VideoRecord::class)
data class VideoRecordFts(
    @ColumnInfo(name = "rowid") @PrimaryKey val id: Int,
    val caption: String?
)
