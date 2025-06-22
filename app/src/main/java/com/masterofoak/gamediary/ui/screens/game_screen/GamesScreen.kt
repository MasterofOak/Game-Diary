package com.masterofoak.gamediary.ui.screens.game_screen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.masterofoak.gamediary.GlobalViewModelProvider
import com.masterofoak.gamediary.ui.scaffold_base.ScaffoldBaseComposable
import com.masterofoak.gamediary.ui.theme.GameDiaryTheme
import com.masterofoak.gamediary.ui.viewmodel.GamesViewModel
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
    AnimatedVisibility(count == 0) {
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
                animationOffset = { pageOffset * 120 },
                sharedTransitionScope,
                animatedContentScope,
                onGameClicked = onGameClicked,
            )
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
            SharedTransitionLayout {
                var state by remember { mutableStateOf(false) }
                AnimatedContent(targetState = state) {
                    @SuppressLint("UnusedContentLambdaTargetStateParameter") it
                    GameScreen(
                        viewModel = viewModel(factory = GlobalViewModelProvider.Factory),
                        onGameClicked = {},
                        onAddGameClicked = { },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}