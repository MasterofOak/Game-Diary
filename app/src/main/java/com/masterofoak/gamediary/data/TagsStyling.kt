package com.masterofoak.gamediary.data

import androidx.compose.ui.graphics.Color

val tagsStyling = listOf<TagStyle>(
    TagStyle(
        tagId = 1,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF3B7D94),
    ),
    TagStyle(
        tagId = 2,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF3B7D94),
    ),
    TagStyle(
        tagId = 3,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF3B7D94),
    ),
    TagStyle(
        tagId = 4,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF3B7D94),
    ),
    TagStyle(
        tagId = 5,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF3B7D94),
    )
)

data class TagStyle(
    val tagId: Int,
    val containerColor: Color,
    val outlineColor: Color,
)
