package com.masterofoak.gamediary.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.masterofoak.gamediary.FILEPROVIDER_AUTHORITY
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun getUriForFile(gameString: String, fileFolder: String, fileExtension: String, context: Context): Uri {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
    val fileDir = File(context.filesDir, fileFolder)
    if (!fileDir.isDirectory) {
        fileDir.mkdirs()
    }
    val filePath = File(fileDir, "file_${gameString}_${timestamp}_${fileExtension}")
    return FileProvider.getUriForFile(context, FILEPROVIDER_AUTHORITY, filePath)
}