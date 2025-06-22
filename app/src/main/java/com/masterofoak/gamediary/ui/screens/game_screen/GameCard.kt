package com.masterofoak.gamediary.ui.screens.game_screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.R
import com.masterofoak.gamediary.model.db_entities.Game
import com.masterofoak.gamediary.model.db_entities.Tag
import com.masterofoak.gamediary.ui.viewmodel.toFullTag

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
internal fun GameCard(
    game: Game,
    tagsList: List<Tag>,
    animationOffset: () -> Float,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onGameClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = { onGameClicked(game.id) },
            )
            .offset {
                IntOffset(0, animationOffset().toInt())
            }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            with(sharedTransitionScope) {
                AsyncImage(
                    model = game.imageUri?.toUri(),
                    contentDescription = null,
                    error = painterResource(R.drawable.ai_slop_placeholder),
                    placeholder = painterResource(R.drawable.ai_slop_placeholder),
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.Low,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "image-${game.id}"),
                            animatedContentScope,
                            boundsTransform = { initialRect, targetRect ->
                                spring(dampingRatio = 1f, stiffness = 450f)
                            }
                        )
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    game.gameName, modifier = Modifier.sharedBounds(
                        sharedTransitionScope.rememberSharedContentState(key = "text-${game.id}"),
                        animatedContentScope
                    )
                )
                Spacer(modifier = Modifier.size(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    items(tagsList) { tag ->
                        com.masterofoak.gamediary.ui.screens.add_game.Tag(
                            tag.toFullTag(),
                            addTagToList = {},
                            isSelected = { false })
                    }
                }
            }
        }
    }
}