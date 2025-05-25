package com.example.gamediary.ui.secondary_composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.gamediary.R
import com.example.gamediary.model.Game
import com.example.gamediary.ui.viewmodel.UserRecordUiState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesDropDown(
    userRecordUiState: UserRecordUiState,
    setGame: (Int?, String?) -> Unit,
    getAllGames: () -> Flow<List<Game>>
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("No selection")) }
    var expanded by remember { mutableStateOf(false) }
    val gamesList by getAllGames().collectAsState(initial = emptyList())
    val currentlySelectedGameImage = userRecordUiState.currentlySelectedGameImage
    val currentlySelectedGameId = userRecordUiState.currentlySelectedGameId
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = { expanded = it }
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                decorationBox = { text ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .padding(8.dp)
                    ) {
                        if (currentlySelectedGameId != null) {
                            DropDownImageIcon(currentlySelectedGameImage?.toUri())
                        }
                        Spacer(Modifier.size(8.dp))
                        text()
                    }
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                matchTextFieldWidth = false
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(172.dp)
                        .height(156.dp)
                ) {
                    item {
                        DropdownMenuItem(
                            text = { Text("No selection") },
                            onClick = {
                                expanded = false
                                textFieldValue = textFieldValue.copy("No selection")
                                setGame(null, null)
                            },
                            modifier = Modifier
                        )
                    }
                    items(gamesList) { it ->
                        DropdownMenuItem(
                            text = { Text(it.gameName) },
                            onClick = {
                                expanded = false
                                textFieldValue = textFieldValue.copy(it.gameName)
                                setGame(it.id, it.imageUri)
                            },
                            leadingIcon = {
                                DropDownImageIcon(it.imageUri?.toUri())
                            },
                            modifier = Modifier
                        )
                    }
                    
                }
            }
        }
    }
}

@Composable
private fun DropDownImageIcon(currentSelectedGameImage: Uri?) {
    AsyncImage(
        model = currentSelectedGameImage ?: R.drawable.ai_slop_placeholder,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.Low,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .aspectRatio(1f)
    )
}