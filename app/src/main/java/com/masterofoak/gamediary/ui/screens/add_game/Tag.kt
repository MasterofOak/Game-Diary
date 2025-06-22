package com.masterofoak.gamediary.ui.screens.add_game

import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.masterofoak.gamediary.ui.viewmodel.FullTag

@Composable
fun Tag(tag: FullTag, addTagToList: (Int) -> Unit, isSelected: (Int) -> Boolean) {
    val isSelected = isSelected(tag.tagId)
    ElevatedFilterChip(
        selected = isSelected,
        onClick = { addTagToList(tag.tagId) },
        label = { Text(tag.tagName, fontWeight = FontWeight.Bold) },
        colors = FilterChipDefaults.elevatedFilterChipColors(
            containerColor = tag.containerColor,
            labelColor = Color(0xFFF9FDFB),
            selectedLabelColor = Color(0xFFF0F0F0),
            iconColor = Color(0xFFF9FDFB),
            selectedTrailingIconColor = Color(0xFF18181C),
            selectedContainerColor = tag.outlineColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = tag.outlineColor,
            selectedBorderColor = tag.containerColor,
            borderWidth = 2.dp,
            selectedBorderWidth = 2.dp
        )
    )
}