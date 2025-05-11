package com.example.gamediary.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.gamediary.R
import com.example.gamediary.model.Game

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDetailScreen(
    game: Game,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = game.imageUri?.toUri(),
                contentDescription = null,
                error = painterResource(R.drawable.ai_slop_placeholder),
                placeholder = painterResource(R.drawable.ai_slop_placeholder),
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.High,
                modifier = Modifier
                    .aspectRatio(1f)
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "image-${game.id}"),
                        animatedContentScope
                    )
            )
        }
    }
}