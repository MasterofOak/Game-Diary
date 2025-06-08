package com.masterofoak.gamediary.utils

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun decodeBitmapFromUri(context: Context, imageUri: Uri): ImageBitmap? {
    try {
        val source = ImageDecoder.createSource(context.contentResolver, imageUri)
        val bitmap = ImageDecoder.decodeBitmap(source).asImageBitmap()
        return bitmap
    } catch (e: Exception) {
        return null
    }
}