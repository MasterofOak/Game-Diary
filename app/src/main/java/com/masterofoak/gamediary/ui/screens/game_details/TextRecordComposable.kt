package com.masterofoak.gamediary.ui.screens.game_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterofoak.gamediary.model.StyleRange
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.utils.buildAnnotatedStringHelper
import kotlinx.serialization.json.Json

@Composable
fun TextRecordComposable(textRecord: TextRecord, onEditRecord: () -> Unit, onDeleteRecord: () -> Unit) {
    val styleRanges = Json.decodeFromString<List<StyleRange>>(textRecord.styleRanges)
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = buildAnnotatedStringHelper(textRecord.content, styleRanges))
            RecordsBottomRow(
                timestamp = textRecord.lastEditedAt ?: textRecord.createdAt,
                text = if (textRecord.lastEditedAt == null) "created at" else "edited",
                isEditAllowed = true,
                onEditClicked = onEditRecord,
                onDeleteClicked = onDeleteRecord
            )
        }
    }
}