package com.masterofoak.gamediary.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun getUriForFile(context: Context, fileFolder: String, fileExtension: String): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
    val fileDir = File(context.filesDir, fileFolder)
    if (!fileDir.isDirectory) {
        fileDir.mkdirs()
    }
    val filePath = File(fileDir, "file_${timeStamp}_${fileExtension}")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", filePath)
}