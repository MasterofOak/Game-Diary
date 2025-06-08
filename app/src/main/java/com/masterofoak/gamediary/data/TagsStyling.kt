package com.masterofoak.gamediary.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

val tagsStyling = listOf<TagStyle>(
    TagStyle(
        tagId = 1,
        containerColor = Color(0xFFFA4441),
        outlineColor = Color(0xFFA42D2B).copy(alpha = 1f),
        tagIcon = null
    ),
    TagStyle(
        tagId = 2,
        containerColor = Color(0xFF228B22),
        outlineColor = Color(0xFF135013),
        tagIcon = null
    ),
    TagStyle(
        tagId = 3,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF225566),
        tagIcon = null
    ),
    TagStyle(
        tagId = 4,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF225566),
        tagIcon = null
    ),
    TagStyle(
        tagId = 5,
        containerColor = Color(0xFF225566),
        outlineColor = Color(0xFF225566),
        tagIcon = null
    )
)

data class TagStyle(
    val tagId: Int,
    val containerColor: Color,
    val outlineColor: Color,
    val tagIcon: ImageVector? = null
)
