package com.example.gamediary.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.gamediary.GlobalViewModelProvider
import com.example.gamediary.R
import com.example.gamediary.model.Game
import com.example.gamediary.model.Tag
import com.example.gamediary.ui.scaffold_base.ScaffoldBaseComposable
import com.example.gamediary.ui.theme.GameDiaryTheme
import com.example.gamediary.ui.viewmodel.GamesViewModel
import com.example.gamediary.ui.viewmodel.toFullTag
import com.example.gamediary.utils.decodeBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameScreen(
    viewModel: GamesViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onGameClicked: (Int) -> Unit,
    onAddGameClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            key = { gamesList[it].id },
            modifier = modifier.fillMaxSize()
        ) { page ->
            val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue
            val game = gamesList[page]
            val gamesTags by viewModel.getGamesTags(game.id).collectAsState(emptyList())
            GameCard(
                game = game,
                tagsList = gamesTags,
                animationOffset = pageOffset * 30,
                sharedTransitionScope,
                animatedContentScope,
                onGameClicked = onGameClicked,
                onLongPress = viewModel::deleteGame
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun GameCard(
    game: Game,
    tagsList: List<Tag>,
    animationOffset: Float,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onGameClicked: (Int) -> Unit,
    onLongPress: (Int) -> Unit,
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
    Card(
        modifier = modifier
            .offset(y = animationOffset.dp)
            .combinedClickable(
                onClick = { onGameClicked(game.id) },
                onLongClick = { onLongPress(game.id) }
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            with(sharedTransitionScope) {
                if (bitmapImage != null) {
                    Image(
                        bitmap = bitmapImage!!,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState(key = "image-${game.id}"),
                                animatedContentScope
                            )
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ai_slop_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    game.gameName, modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "text-${game.id}"),
                        animatedContentScope
                    )
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    items(tagsList) { tag ->
                        Tag(tag.toFullTag(), addTagToList = {}, isSelected = { false })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Preview
fun GameScreen_Preview() {
    GameDiaryTheme {
        ScaffoldBaseComposable(
            topBarComposable = {},
            floatingActionButtonComposable = { },
            bottomBarComposable = {},
            modifier = Modifier
        ) {
            SharedTransitionScope {
                var state by remember { mutableStateOf(false) }
                AnimatedContent(targetState = state) {
                    @SuppressLint("UnusedContentLambdaTargetStateParameter") it
                    GameScreen(
                        viewModel = viewModel(factory = GlobalViewModelProvider.Factory),
                        onGameClicked = {},
                        onAddGameClicked = { },
                        sharedTransitionScope = this@SharedTransitionScope,
                        animatedContentScope = this,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}