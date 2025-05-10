package com.example.gamediary.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "GamesTags",
    primaryKeys = ["gameId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("gameId"), Index("tagId")]
)
data class GamesTags(
    val gameId: Int,
    val tagId: Int
)
