package com.masterofoak.gamediary.model.fts_tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.masterofoak.gamediary.model.db_entities.Tag

@Entity(tableName = "tags_fts")
@Fts4(contentEntity = Tag::class)
data class TagFts(
    @ColumnInfo(name = "rowid") @PrimaryKey val id: Int,
    @ColumnInfo(name = "tag_name") val tagName: String
)
