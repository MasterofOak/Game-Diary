package com.masterofoak.gamediary.model.fts_tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.masterofoak.gamediary.model.db_entities.TextRecord


@Entity(tableName = "text_records_fts")
@Fts4(contentEntity = TextRecord::class)
data class TextRecordFts(
    @ColumnInfo(name = "rowid") @PrimaryKey val id: Int,
    val content: String,
)
