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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
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
fun AddImageRecord(updateState: (String) -> Unit) {
    val context = LocalContext.current
    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(capturedImageUri) {
        if (capturedImageUri != null) {
            updateState(capturedImageUri.toString())
            println("Link updated")
        }
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            capturedImageUri = uri
            context.contentResolver.takePersistableUriPermission(uri, flag)
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess: Boolean ->
            if (isSuccess) {
            } else {
                capturedImageUri = null
            }
        }
    )
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            val imageUri = getUriForFile(context, "images", ".jpg")
            capturedImageUri = imageUri
            cameraLauncher.launch(imageUri)
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
                    photoPickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
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
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        val imageUri = getUriForFile(context, "images", ".jpg")
                        capturedImageUri = imageUri
                        cameraLauncher.launch(imageUri)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Take a photo")
        }
    }
}