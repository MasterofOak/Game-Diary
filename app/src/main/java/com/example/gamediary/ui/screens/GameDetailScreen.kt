package com.example.gamediary.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.gamediary.ui.viewmodel.GamesViewModel
import com.example.gamediary.utils.decodeBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDetailScreen(
    viewModel: GamesViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val game = uiState.value.currentGame!!
    val context = LocalContext.current
    var bitmapImage by remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(game.imageUri) {
        if (game.imageUri != null) {
            withContext(Dispatchers.IO) {
                bitmapImage = decodeBitmapFromUri(context, game.imageUri.toUri())
            }
        }
    }
    with(sharedTransitionScope) {
        Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            bitmapImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = "",
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "image-${game.id}"),
                        animatedContentScope
                    )
                )
            }
            Text(
                game.gameName, modifier = Modifier.sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key = "text-${game.id}"),
                    animatedContentScope
                )
            )
        }
        
    }
}