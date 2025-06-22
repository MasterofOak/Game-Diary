package com.masterofoak.gamediary.ui.screens.add_game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.masterofoak.gamediary.ui.viewmodel.FullTag

@Composable
internal fun Tags(
    tagsList: List<FullTag>,
    selectedTags: List<Int>,
    addTagToList: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isTagsClicked by remember { mutableStateOf(false) }
    val tagIconRotation by animateFloatAsState(targetValue = if (isTagsClicked) 225f else 0f, label = "tagIconRotation")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedAssistChip(
            onClick = { isTagsClicked = !isTagsClicked },
            label = { Text("Tags") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier
                        .rotate(tagIconRotation)
                )
            },
            shape = RoundedCornerShape(4.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(":")
        Spacer(modifier = Modifier.size(4.dp))
        AnimatedVisibility(isTagsClicked, enter = expandHorizontally(), exit = shrinkHorizontally()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                    .fillMaxWidth(if (isTagsClicked) 1f else 0f)
            ) {
                items(tagsList, key = { it.tagId }) { tag ->
                    Tag(tag, addTagToList, isSelected = { selectedTags.contains(it) })
                }
            }
        }
    }
}