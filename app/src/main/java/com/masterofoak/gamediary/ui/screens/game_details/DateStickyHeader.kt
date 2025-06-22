package com.masterofoak.gamediary.ui.screens.game_details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun DateStickyHeader(
    date: String, isDropDownClicked: Boolean, expandedDropDownMap: SnapshotStateMap<String, Boolean>
) {
    val dropDownRotation by animateFloatAsState(
        targetValue = if (isDropDownClicked) 180f else 0f, label = "DropDownArrowRotation"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color.Gray, CircleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "[ $date ]", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.size(8.dp))
        HorizontalDivider(modifier = Modifier.weight(1f), 2.dp)
        IconButton(onClick = { expandedDropDownMap[date] = !isDropDownClicked }) {
            Icon(
                imageVector = Icons.Default.ArrowDropUp,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .rotate(dropDownRotation)
            )
        }
    }
}