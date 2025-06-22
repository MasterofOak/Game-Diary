package com.masterofoak.gamediary.ui.screens.add_game

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.masterofoak.gamediary.model.db_entities.Game
import com.masterofoak.gamediary.model.db_entities.Tag
import com.masterofoak.gamediary.ui.theme.GameDiaryTheme
import com.masterofoak.gamediary.ui.viewmodel.AddGameUiState
import com.masterofoak.gamediary.ui.viewmodel.toFullTag

@Composable
fun AddGameScreen(
    addGameUiState: AddGameUiState,
    gameToUpdate: Game?,
    addGame: (String, String?) -> Unit,
    addSelectedTagToList: (Int) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
    var gameNameTextField by rememberSaveable { mutableStateOf(gameToUpdate?.gameName ?: "") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(gameToUpdate?.imageUri?.toUri()) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            context.contentResolver.takePersistableUriPermission(uri, flag)
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GameImage(
                selectImageFromGallery = {
                    photoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                },
                imageUri = imageUri
            )
            TextField(
                value = gameNameTextField,
                onValueChange = { gameNameTextField = it },
                label = { Text("Game Name:") },
                placeholder = { Text("Half-Life 2") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Text,
                    showKeyboardOnFocus = true
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                isError = gameNameTextField.isNotEmpty() && gameNameTextField.length < 2,
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            )
        }
        Tags(
            tagsList = addGameUiState.tagsList,
            selectedTags = addGameUiState.selectedTags,
            addTagToList = addSelectedTagToList
        )
        Button(onClick = {
            addGame(gameNameTextField, imageUri?.toString())
            navigateUp()
        }) {
            Text("Add Game")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddGameScreen_Preview() {
    GameDiaryTheme {
        Tag(tag = Tag(1, "Action").toFullTag(), addTagToList = { 1 }, isSelected = { true })
    }
}