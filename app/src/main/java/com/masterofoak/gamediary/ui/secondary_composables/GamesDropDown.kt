package com.masterofoak.gamediary.ui.secondary_composables

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.R
import com.masterofoak.gamediary.model.Game
import com.masterofoak.gamediary.ui.viewmodel.UserRecordUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesDropDown(
    userRecordUiState: UserRecordUiState,
    setGame: (Int?, String?) -> Unit,
    gamesList: List<Game>
) {
    val elementsHeight = 48.dp
    var textFieldValue by remember { mutableStateOf(TextFieldValue("No selection")) }
    var expanded by remember { mutableStateOf(false) }
    val dropDownRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = "DropDownArrowRotation"
    )
    val currentlySelectedGameImage = userRecordUiState.currentlySelectedGameImage
    val currentlySelectedGameId = userRecordUiState.currentlySelectedGameId
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            decorationBox = { text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    if (currentlySelectedGameId != null) {
                        DropDownImageIcon(currentlySelectedGameImage?.toUri())
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    text()
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.ArrowDropUp,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .rotate(dropDownRotation)
                    )
                }
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .height(elementsHeight * 1.75f)
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            matchTextFieldWidth = true,
            modifier = Modifier.onSizeChanged { parentSize = it }
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .width(parentSize.width.dp)
                    .height(elementsHeight * 4)
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
                            .weight(1f)
                            .height(elementsHeight)
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
                            .weight(1f)
                            .height(elementsHeight)
                    )
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
            .size(48.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
    )
    
}

@Composable
@Preview
fun GamesDropDown_Preview() {
    Surface {
        GamesDropDown(
            UserRecordUiState(),
            setGame = { arg1, arg -> },
            listOf<Game>()
        )
    }
}