package com.masterofoak.gamediary.ui.scaffold_base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionsButtonsList(
    onGameFabClicked: () -> Unit,
    onTextRecordFabClicked: () -> Unit,
    onImageRecordFabClicked: () -> Unit,
    onVideoRecordFabClicked: () -> Unit
) {
    var isFabClicked by remember { mutableStateOf(false) }
    val fabIconRotation by animateFloatAsState(targetValue = if (isFabClicked) 45f else 0f, label = "FABRotation")
    LazyColumn(
        modifier = Modifier,
        horizontalAlignment = Alignment.End
    ) {
        item {
            AnimatedVisibility(
                isFabClicked,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { onVideoRecordFabClicked(); isFabClicked = !isFabClicked },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Video Record")
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(imageVector = Icons.Default.Videocam, "Add video record action button")
                }
            }
        }
        item {
            AnimatedVisibility(
                isFabClicked,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { onImageRecordFabClicked(); isFabClicked = !isFabClicked },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Image Record")
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(imageVector = Icons.Default.Image, "Add image record action button")
                }
            }
        }
        item {
            AnimatedVisibility(
                isFabClicked,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { onTextRecordFabClicked(); isFabClicked = !isFabClicked }, modifier = Modifier
                        .padding(4.dp)
                ) {
                    Text("Text Record")
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.Notes, "Add text record action button")
                }
            }
        }
        item {
            AnimatedVisibility(
                isFabClicked,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { onGameFabClicked(); isFabClicked = !isFabClicked },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Game")
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(imageVector = Icons.Default.VideogameAsset, "Add game action button")
                }
            }
        }
        item {
            FloatingActionButton(
                onClick = {
                    isFabClicked = !isFabClicked
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Open action buttons",
                    modifier = Modifier.rotate(fabIconRotation)
                )
            }
        }
    }
}