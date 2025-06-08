package com.masterofoak.gamediary.ui.add_records_composables

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.masterofoak.gamediary.utils.getUriForFile

@Composable
fun AddVideoRecord(updateState: (String) -> Unit) {
    val context = LocalContext.current
    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
    var capturedVideoUri by remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(capturedVideoUri) {
        if (capturedVideoUri != null) {
            updateState(capturedVideoUri.toString())
            println("Link updated")
        }
    }
    val pickVideoLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            capturedVideoUri = uri
            context.contentResolver.takePersistableUriPermission(uri, flag)
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
        onResult = { isSuccess: Boolean ->
            if (isSuccess) {
                // Image saved to `capturedImageUri`. Now you can display  it or process it.
                // The `capturedImageUri` state already holds the URI.
            } else {
                capturedVideoUri = null
            }
        }
    )
    val videoPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] == true
        if (cameraGranted && audioGranted) {
            val videoUri = getUriForFile(context, "videos", ".mp4")
            capturedVideoUri = videoUri
            cameraLauncher.launch(videoUri)
        }
    }
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    pickVideoLauncher.launch(PickVisualMediaRequest(PickVisualMedia.VideoOnly))
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Choose from gallery")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    val audioPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                    val permissionsToRequest = mutableListOf<String>()
                    if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                        permissionsToRequest.add(Manifest.permission.CAMERA)
                    }
                    if (audioPermission != PackageManager.PERMISSION_GRANTED) {
                        permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
                    }
                    
                    if (permissionsToRequest.isNotEmpty()) {
                        videoPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
                    } else {
                        val videoUri = getUriForFile(context, "videos", ".mp4")
                        capturedVideoUri = videoUri
                        cameraLauncher.launch(videoUri)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Record a video")
        }
    }
}