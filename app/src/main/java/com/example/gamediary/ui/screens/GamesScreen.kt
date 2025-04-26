package com.example.gamediary.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamediary.R
import com.example.gamediary.ui.theme.GameDiaryTheme
import com.example.gamediary.ui.viewmodel.GamesViewModel
import com.example.gamediary.utils.decodeBitmapFromUri
import kotlin.math.absoluteValue

@Composable
fun GameScreen(
    viewModel: GamesViewModel = viewModel(factory = GamesViewModel.factory),
    onGameClicked: () -> Unit,
    onAddGameClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val gamesList = uiState.value.gamesList
    val count = gamesList.size
    val pagerState = rememberPagerState(pageCount = { count })
    if (count == 0) {
        AddGameCard(onAddGameClicked = onAddGameClicked)
    }
    AnimatedVisibility(count >= 1, enter = fadeIn()) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 3,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxSize()
        ) { page ->
            val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue
            val game = gamesList[page]
            val gameId = game.id
            val gameName = game.gameName
            val imageUri = game.imageUri
            var bitmapImage by remember { mutableStateOf<ImageBitmap?>(null) }
            LaunchedEffect(imageUri) {
                bitmapImage = null
                imageUri?.let { bitmapImage = decodeBitmapFromUri(context, it.toUri()) }
            }
            GameCard(
                gameName = gameName,
                gameImage = bitmapImage,
                animationOffset = pageOffset * 30,
                onGameClicked = { onGameClicked() },
                onLongPress = { viewModel.deleteGame(gameId) }
            )
        }
    }
}

@Composable
private fun AddGameCard(onAddGameClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .size(128.dp)
                .clickable { onAddGameClicked() }
        ) {
            Image(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Game",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameCard(
    gameName: String,
    gameImage: ImageBitmap?,
    animationOffset: Float,
    onGameClicked: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .offset(y = animationOffset.dp)
            .combinedClickable(
                onClick = { onGameClicked() },
                onLongClick = { onLongPress() }
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            if (gameImage != null) {
                Image(
                    bitmap = gameImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.size(32.dp))
            Text(gameName)
        }
    }
}

@Composable
@Preview
fun GameScreen_Preview() {
    GameDiaryTheme {
        GameScreen(
            modifier = Modifier.fillMaxSize(), onGameClicked = {},
            viewModel = TODO(),
            onAddGameClicked = { }
        )
    }
}