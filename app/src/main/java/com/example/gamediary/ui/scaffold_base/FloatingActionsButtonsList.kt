package com.example.gamediary.ui.scaffold_base

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
fun FloatingActionsButtonsList(onGameFabClicked: () -> Unit, onTextRecordFabClicked: () -> Unit) {
    var isFabClicked by remember { mutableStateOf(false) }
    val fabIconRotation by animateFloatAsState(targetValue = if (isFabClicked) 45f else 0f, label = "FABRotation")
    LazyColumn(
        modifier = Modifier,
        horizontalAlignment = Alignment.End
    ) {
        items(1) {
            AnimatedVisibility(
                isFabClicked,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ExtendedFloatingActionButton(onClick = {}, modifier = Modifier.padding(4.dp)) {
                    Text("Something")
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(imageVector = Icons.Default.Add, "Add" + " Button")
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
                    Icon(imageVector = Icons.AutoMirrored.Filled.Notes, "Add Text Record Action Button")
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
                    onClick = { onGameFabClicked(); isFabClicked = !isFabClicked }, modifier = Modifier
                        .padding(4.dp)
                ) {
                    Text("Game")
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(imageVector = Icons.Default.VideogameAsset, "Add Game Action Button")
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
                    contentDescription = "Add Button",
                    modifier = Modifier.rotate(fabIconRotation)
                )
            }
        }
    }
}