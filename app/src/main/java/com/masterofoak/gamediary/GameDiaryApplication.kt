package com.masterofoak.gamediary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.masterofoak.gamediary.database.DBContainer

class GameDiaryApplication : Application() {
    
    lateinit var container: DBContainer
    override fun onCreate() {
        super.onCreate()
        container = DBContainer(this)
    }
}

class MyFileProvider() : FileProvider()

@SuppressLint("RememberReturnType")
@Composable
fun RequestCameraPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    )
    
    // Check permission status and launch request if needed
    val cameraPermissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED) {
        // Permission already granted
        onPermissionGranted()
    } else {
        // Request permission
        remember { permissionLauncher.launch(Manifest.permission.CAMERA) }
        // The above `remember` is important to avoid launching the permission request on every recomposition.
        // You might want to show a UI element (e.g., a button) to trigger the request explicitly
        // instead of launching it automatically if you want more control.
    }
}