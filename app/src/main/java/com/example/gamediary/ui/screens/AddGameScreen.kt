package com.example.gamediary.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gamediary.ui.theme.GameDiaryTheme
import com.example.gamediary.ui.viewmodel.GamesViewModel
import com.example.gamediary.utils.decodeBitmapFromUri

@Composable
fun AddGameScreen(
    viewModel: GamesViewModel, navigateUp: () -> Unit, modifier: Modifier = Modifier
) {
    var gameNameTextField by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri -> imageUri = uri }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
                .background(Color.Transparent)
                .clickable { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri == null) {
                Image(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = "")
            } else {
                val context = LocalContext.current
                Image(
                    bitmap = decodeBitmapFromUri(context, imageUri!!),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(4.dp, Color.Red, CircleShape)
                
                )
            }
        }
        TextField(
            value = gameNameTextField,
            onValueChange = { gameNameTextField = it },
            label = { Text("Game Name:") },
            placeholder = { Text("Half-Life 2") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
        )
        Button(onClick = { viewModel.addGame(gameNameTextField, imageUri); navigateUp() }) {
            Text("Add Game")
        }
    }
}

@Preview
@Composable
fun AddGameScreen_Preview() {
    GameDiaryTheme {
        AddGameScreen(
            viewModel = TODO(),
            navigateUp = TODO(),
            modifier = TODO()
        )
    }
}