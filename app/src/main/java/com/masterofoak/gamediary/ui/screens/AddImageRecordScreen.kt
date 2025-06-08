package com.masterofoak.gamediary.ui.screens

import android.R.attr.mimeType
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.model.ImageRecord
import com.masterofoak.gamediary.ui.viewmodel.UserRecordUiState

@Composable
fun AddImageRecordScreen(
    addImageRecord: (ImageRecord) -> Unit,
    modifier: Modifier = Modifier,
    userRecordUiState: UserRecordUiState
) {
    val context = LocalContext.current
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val photoPicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri
            Toast.makeText(context, "Selected URI: $uri\nMIME Type: $mimeType", Toast.LENGTH_LONG).show()
            println(uri)
            
        } else {
            Toast.makeText(context, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(4.dp, Color.DarkGray, CircleShape)
                .background(Color.LightGray)
                .clickable { photoPicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri == null) {
                Image(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(4.dp, Color.Red, CircleShape)
                
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text("Game Image")
        Button(
            onClick = {
                addImageRecord(
                    ImageRecord(
                        gameId = userRecordUiState.currentlySelectedGameId!!,
                        imageUri = imageUri.toString(),
                        createdAt = System.currentTimeMillis()
                    )
                )
            }, enabled =
                userRecordUiState
                    .currentlySelectedGameId !=
                        null
        ) {
            Text("Add")
        }
    }
}