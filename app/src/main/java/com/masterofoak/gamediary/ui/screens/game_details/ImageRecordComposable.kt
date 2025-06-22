package com.masterofoak.gamediary.ui.screens.game_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.model.db_entities.ImageRecord
import com.masterofoak.gamediary.ui.secondary_composables.AddCaptionDialog

@Composable
fun ImageRecordComposable(
    imageRecord: ImageRecord,
    onEditRecord: () -> Unit,
    onDeleteRecord: () -> Unit,
    onAddCaption: (Int, String) -> Unit
) {
    var isAddCaptionDialogOpen by remember { mutableStateOf(false) }
    if (isAddCaptionDialogOpen) {
        AddCaptionDialog(
            onDismissRequest = { isAddCaptionDialogOpen = !isAddCaptionDialogOpen },
            onConfirm = { onAddCaption(imageRecord.id, it) }
        )
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = imageRecord.imageUri,
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier =
                    Modifier.aspectRatio(16f / 9f)
            )
            if (imageRecord.caption != null) {
                Row {
                    Text(imageRecord.caption, fontStyle = FontStyle.Italic)
                }
            } else {
                Row(
                    modifier = Modifier
                        .clickable {
                            isAddCaptionDialogOpen = !isAddCaptionDialogOpen
                        }
                        .height(32.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add caption")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text("Add caption", fontStyle = FontStyle.Italic, fontSize = 12.sp)
                }
                
            }
            RecordsBottomRow(
                timestamp = imageRecord.lastEditedAt ?: imageRecord.createdAt,
                text = if (imageRecord.lastEditedAt == null) "created at" else "edited",
                isEditAllowed = false,
                onEditClicked = onEditRecord,
                onDeleteClicked = onDeleteRecord
            )
            
        }
    }
}