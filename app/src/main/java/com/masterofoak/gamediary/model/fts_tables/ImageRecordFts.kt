package com.masterofoak.gamediary.model.fts_tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.masterofoak.gamediary.model.db_entities.ImageRecord

@Entity(tableName = "image_records_fts")
@Fts4(contentEntity = ImageRecord::class)
data class ImageRecordFts(
    @ColumnInfo(name = "rowid") @PrimaryKey val id: Int,
    val caption: String?,
)