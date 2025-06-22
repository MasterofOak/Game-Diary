package com.masterofoak.gamediary.ui.screens.game_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.masterofoak.gamediary.utils.getFormatedDateWithHours

@Composable
internal fun RecordsBottomRow(
    timestamp: Long,
    text: String,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    isEditAllowed: Boolean
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            if (isEditAllowed) {
                IconButton(onClick = { onEditClicked() }, modifier = Modifier.size(20.dp)) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit record")
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
            IconButton(onClick = { onDeleteClicked() }, modifier = Modifier.size(20.dp)) {
                Icon(Icons.Filled.DeleteForever, contentDescription = "Delete record")
            }
        }
        Text(
            text = "$text : ${getFormatedDateWithHours(timestamp = timestamp, context = context)}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}