package com.masterofoak.gamediary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tag_name") val tagName: String,
    @ColumnInfo(name = "is_custom_tag") val isCustomTag: Boolean = false
)
