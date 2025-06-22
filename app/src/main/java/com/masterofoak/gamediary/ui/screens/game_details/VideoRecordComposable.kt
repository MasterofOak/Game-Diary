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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.masterofoak.gamediary.model.db_entities.VideoRecord
import com.masterofoak.gamediary.ui.secondary_composables.AddCaptionDialog
import com.masterofoak.gamediary.ui.viewmodel.VideoPlayerViewModel

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoRecordComposable(
    videoRecord: VideoRecord,
    onEditRecord: () -> Unit,
    onDeleteRecord: () -> Unit,
    onAddCaption: (Int, String) -> Unit
) {
    var isAddCaptionDialogOpen by remember { mutableStateOf(false) }
    if (isAddCaptionDialogOpen) {
        AddCaptionDialog(
            onDismissRequest = { isAddCaptionDialogOpen = !isAddCaptionDialogOpen },
            onConfirm = { onAddCaption(videoRecord.id, it) }
        )
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
        Column(
            modifier = Modifier
        ) {
            val lifecycleOwner = LocalLifecycleOwner.current
            // Initialize ExoPlayer and remember it across recompositions
            val videoPlayerViewModel: VideoPlayerViewModel = viewModel(
                key = "${videoRecord.videoUri}_${videoRecord.createdAt}"
            )
            DisposableEffect(videoRecord.videoUri) {
                videoPlayerViewModel.setVideoUrl(videoRecord.videoUri)
                onDispose { }
            }
            DisposableEffect(lifecycleOwner, videoPlayerViewModel.exoPlayer) {
                videoPlayerViewModel.setVideoUrl(videoRecord.videoUri)
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_STOP -> {
                            videoPlayerViewModel.exoPlayer.pause()
                        }
                        
                        Lifecycle.Event.ON_DESTROY -> {
                            videoPlayerViewModel.exoPlayer.release()
                        }
                        
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                // Clean up the observer when the composable leaves the composition
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = videoPlayerViewModel.exoPlayer
                        useController = true
                        controllerShowTimeoutMs = 2000
                        setShowPreviousButton(false)
                        setShowNextButton(false)
                    }
                },
                modifier = Modifier
                    .height(256.dp)
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            )
            if (videoRecord.caption != null) {
                Row {
                    Text(videoRecord.caption, fontStyle = FontStyle.Italic)
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
                timestamp = videoRecord.lastEditedAt ?: videoRecord.createdAt,
                text = if (videoRecord.lastEditedAt == null) "created at" else "edited",
                isEditAllowed = false,
                onEditClicked = onEditRecord,
                onDeleteClicked = onDeleteRecord
            )
        }
    }
}