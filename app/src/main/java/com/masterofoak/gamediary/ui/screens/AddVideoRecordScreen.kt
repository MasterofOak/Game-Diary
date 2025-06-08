package com.masterofoak.gamediary.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.masterofoak.gamediary.model.VideoRecord
import com.masterofoak.gamediary.ui.viewmodel.UserRecordUiState

@Composable
fun AddVideoRecordScreen(
    addVideoRecord: (VideoRecord) -> Unit,
    userRecordUiState: UserRecordUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mimeType by remember { mutableStateOf<String?>(null) }
    var videoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val videoPicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            videoUri = uri
            mimeType = context.contentResolver.getType(uri) // Get the actual MIME type of the selected file
            Toast.makeText(context, "Selected URI: $uri\nMIME Type: $mimeType", Toast.LENGTH_LONG).show()
            println(uri)
            
        } else {
            mimeType = null
            Toast.makeText(context, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        Button(onClick = { videoPicker.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/gif"))) }) {
            Text("Open gallery")
        }
        Button(onClick = { videoPicker.launch(PickVisualMediaRequest(PickVisualMedia.VideoOnly)) }) {
            Text("Open gallery")
        }
    }
}