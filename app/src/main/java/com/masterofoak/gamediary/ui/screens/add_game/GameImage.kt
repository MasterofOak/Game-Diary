package com.masterofoak.gamediary.ui.screens.add_game

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
internal fun GameImage(
    selectImageFromGallery: () -> Unit,
    imageUri: Uri?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(4.dp, Color.DarkGray, CircleShape)
                .background(Color.LightGray)
                .clickable { selectImageFromGallery() },
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
    }
}