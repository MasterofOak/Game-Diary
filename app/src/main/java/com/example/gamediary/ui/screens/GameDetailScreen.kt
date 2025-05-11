package com.example.gamediary.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import com.example.gamediary.R
import com.example.gamediary.model.Game
import com.example.gamediary.utils.decodeBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDetailScreen(
    game: Game,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bitmapImage by remember { mutableStateOf<ImageBitmap?>(null) }
    if (game.imageUri != null) {
        LaunchedEffect(game.imageUri) {
            withContext(Dispatchers.IO) {
                bitmapImage = decodeBitmapFromUri(context, game.imageUri.toUri())
            }
        }
    }
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        with(sharedTransitionScope) {
            if (bitmapImage != null) {
                Image(
                    bitmap = bitmapImage!!,
                    contentDescription = "",
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "image-${game.id}"),
                        animatedContentScope
                    )
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ai_slop_placeholder),
                    contentDescription = "",
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "image-${game.id}"),
                        animatedContentScope
                    )
                )
            }
        }
    }
}