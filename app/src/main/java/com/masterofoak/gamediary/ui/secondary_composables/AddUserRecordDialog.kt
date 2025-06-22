package com.masterofoak.gamediary.ui.secondary_composables

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.masterofoak.gamediary.model.*
import com.masterofoak.gamediary.model.db_entities.ImageRecord
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.model.db_entities.VideoRecord
import com.masterofoak.gamediary.ui.add_records_composables.AddImageRecord
import com.masterofoak.gamediary.ui.add_records_composables.AddTextRecord
import com.masterofoak.gamediary.ui.add_records_composables.AddVideoRecord
import com.masterofoak.gamediary.ui.viewmodel.UserRecordViewModel
import kotlinx.serialization.json.Json


@Composable
fun AddUserRecordDialog(
    userRecordViewModel: UserRecordViewModel,
    recordType: RecordType,
    isUpdateMode: Boolean,
    recordToUpdate: Records?,
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
                            RecordType.TEXT ->
                                if (!isUpdateMode) {
                                    userRecordViewModel.addTextRecords(
                                        TextRecord(
                                            gameId = userRecordUiState.currentlySelectedGameId!!,
                                            content = userRecordUiState.textContent!!,
                                            styleRanges = Json.encodeToString(userRecordUiState.styleRanges),
                                            createdAt = System.currentTimeMillis()
                                        )
                                    )
                                } else {
                                    println("I am here")
                                    val textRecord = recordToUpdate as TextRecord
                                    userRecordViewModel.updateTextRecord(
                                        textRecord.copy(
                                            content = userRecordUiState.textContent!!,
                                            styleRanges = Json.encodeToString(userRecordUiState.styleRanges),
                                            lastEditedAt = System.currentTimeMillis()
                                        )
                                    )
                                }
                            
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
                enabled = (userRecordUiState.currentlySelectedGameId != null) &&
                        (userRecordUiState.textContent != null ||
                                userRecordUiState.imageUri != null ||
                                userRecordUiState.videoUri != null)
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
                            recordToUpdate = if (recordToUpdate != null) recordToUpdate as TextRecord else null,
                            updateState = userRecordViewModel::updateTextRecordState,
                        )
                        
                        RecordType.IMAGE -> AddImageRecord(
                            gameString = "id${userRecordUiState.currentlySelectedGameId}_" +
                                    "${userRecordUiState.currentlySelectedGameName}",
                            updateState = userRecordViewModel::updateImageRecordState
                        )
                        
                        RecordType.VIDEO -> AddVideoRecord(
                            gameString =
                                "id${userRecordUiState.currentlySelectedGameId}_" +
                                        "${userRecordUiState.currentlySelectedGameName}",
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
                            imageVector = Icons.Default.CheckCircle,
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