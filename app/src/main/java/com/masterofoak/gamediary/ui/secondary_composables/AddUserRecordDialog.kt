package com.masterofoak.gamediary.ui.secondary_composables

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.R
import com.masterofoak.gamediary.model.*
import com.masterofoak.gamediary.ui.add_records_composables.AddImageRecord
import com.masterofoak.gamediary.ui.add_records_composables.AddTextRecord
import com.masterofoak.gamediary.ui.add_records_composables.AddVideoRecord
import com.masterofoak.gamediary.ui.viewmodel.UserRecordViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json


@Composable
fun AddUserRecordDialog(
    userRecordViewModel: UserRecordViewModel,
    recordType: RecordType,
    onDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val userRecordUiState = userRecordViewModel.uiState.collectAsState().value
    var currentStep by remember { mutableIntStateOf(1) }
    val confirmButtonText = if (currentStep != 2) "Next" else "OK"
    val columnHeight = if (recordType == RecordType.TEXT || currentStep != 1) 320.dp else 96.dp
    AlertDialog(
        onDismissRequest = { onDialogDismiss(); userRecordViewModel.resetUiState() },
        dismissButton = {
            if (currentStep != 2) {
                TextButton(onClick = { onDialogDismiss(); userRecordViewModel.resetUiState() }) {
                    Text(text = "Close")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (currentStep != 2) {
                        currentStep += 1
                        when (recordType) {
                            RecordType.TEXT -> userRecordViewModel.addTextRecords(
                                TextRecord(
                                    gameId = userRecordUiState.currentlySelectedGameId!!,
                                    content = userRecordUiState.textContent!!,
                                    styleRanges = Json.encodeToString(userRecordUiState.styleRanges),
                                    createdAt = System.currentTimeMillis()
                                )
                            )
                            
                            RecordType.IMAGE -> userRecordViewModel.addImageRecords(
                                ImageRecord(
                                    gameId = userRecordUiState.currentlySelectedGameId!!,
                                    imageUri = userRecordUiState.imageUri!!,
                                    createdAt = System.currentTimeMillis()
                                )
                            )
                            
                            RecordType.VIDEO -> userRecordViewModel.addVideoRecords(
                                VideoRecord(
                                    gameId = userRecordUiState.currentlySelectedGameId!!,
                                    videoUri = userRecordUiState.videoUri!!,
                                    createdAt = System.currentTimeMillis()
                                )
                            )
                        }
                    } else {
                        onDialogDismiss()
                        userRecordViewModel.resetUiState()
                    }
                },
                enabled = userRecordUiState.textContent != null ||
                        userRecordUiState.imageUri != null ||
                        userRecordUiState.videoUri != null
            ) {
                Text(confirmButtonText)
            }
        },
        title = {
            if (currentStep == 1) {
                val gamesList by userRecordViewModel.getAllGamesList().collectAsState(initial = emptyList())
                GamesDropDown(
                    userRecordUiState = userRecordUiState,
                    setGame = userRecordViewModel::setSelectedGame,
                    gamesList = gamesList
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.height(columnHeight),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    currentStep == 1,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut(),
                    modifier = Modifier.fillMaxSize()
                
                )
                {
                    when (recordType) {
                        RecordType.TEXT -> AddTextRecord(
                            updateState = userRecordViewModel::updateTextRecordState
                        )
                        
                        RecordType.IMAGE -> AddImageRecord(
                            updateState = userRecordViewModel::updateImageRecordState
                        )
                        
                        RecordType.VIDEO -> AddVideoRecord(
                            updateState = userRecordViewModel::updateVideoRecordState
                        )
                    }
                }
                AnimatedVisibility(
                    currentStep == 2,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sos,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.5f)
                        )
                        Text("Operation was successful")
                    }
                }
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
        modifier = modifier
    )
}


@Composable
private fun Picker(getAllGames: () -> Flow<List<Game>>) {
    val gamesList by getAllGames().collectAsState(initial = emptyList())
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxHeight(0.9f)
    ) {
        items(gamesList) { game ->
            var isClicked by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .height(128.dp)
                    .clickable { isClicked = !isClicked },
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (isClicked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.error
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    AsyncImage(
                        model = game.imageUri?.toUri() ?: R.drawable.ai_slop_placeholder,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        filterQuality = FilterQuality.Low,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(game.gameName)
                }
            }
            
        }
    }
}