package com.masterofoak.gamediary.utils

import android.net.Uri
import java.io.File

fun deleteFileFromUri(fileUri: Uri?, filesDir: File, filePath: String) {
    if (fileUri == null) {
        println("Error: URI is null.")
        return
    }
    val fileName = fileUri.lastPathSegment
    if (fileName != null) {
        val fullFileDir = File(filesDir, filePath)
        val fileToDelete = File(fullFileDir, fileName)
        if (fileToDelete.exists()) {
            fileToDelete.delete()
            return
        }
    }
}