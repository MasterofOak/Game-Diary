package com.example.gamediary.ui.screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.gamediary.R
import com.example.gamediary.model.Game
import com.example.gamediary.model.StyleRange
import com.example.gamediary.model.TextRecord
import com.example.gamediary.ui.theme.GameDiaryTheme
import com.example.gamediary.utils.buildAnnotatedStringHelper
import com.example.gamediary.utils.getFormatedDate
import com.example.gamediary.utils.getFormatedDateWithHours
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDetailScreen(
    game: Game,
    getAllTextRecords: (Int) -> Flow<List<TextRecord>>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    val textRecordsList by getAllTextRecords(game.id).collectAsState(initial = emptyList())
    val lazyColumnState = rememberLazyListState()
    val maxSize = 300.dp
    var currentImageSize by remember { mutableStateOf(maxSize) }
    var imageScale by remember { mutableFloatStateOf(1f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newSize = currentImageSize + delta.dp
                val prevSize = currentImageSize
                currentImageSize = newSize.coerceIn(100.dp, maxSize)
                val consumed = currentImageSize - prevSize
                imageScale = currentImageSize / maxSize
                return Offset(0f, consumed.value)
            }
            
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                return super.onPostScroll(consumed, available, source)
            }
        }
    }
    LazyColumn(
        state = lazyColumnState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        item {
            with(sharedTransitionScope) {
                AsyncImage(
                    model = game.imageUri?.toUri(),
                    contentDescription = null,
                    error = painterResource(R.drawable.ai_slop_placeholder),
                    placeholder = painterResource(R.drawable.ai_slop_placeholder),
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.High,
                    modifier = Modifier
                        .size(currentImageSize)
                        .graphicsLayer {
                            scaleX = imageScale
                            scaleY = imageScale
                            // Center the image vertically as it scales
                            translationY = -(maxSize.toPx() - currentImageSize.toPx()) / 2f
                        }
                        .aspectRatio(1f)
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "image-${game.id}"),
                            animatedContentScope,
                            boundsTransform = { initialRect, targetRect ->
                                spring(dampingRatio = 1f, stiffness = 450f)
                            }
                        )
                )
            }
        }
        items(textRecordsList) { record ->
            Record(record)
        }
    }
}

@Composable
private fun Record(textRecord: TextRecord, modifier: Modifier = Modifier) {
    var isDropDownClicked by remember { mutableStateOf(false) }
    val dropDownRotation by animateFloatAsState(
        targetValue = if (isDropDownClicked) 180f else 0f, label = "DropDownArrowRotation"
    )
    val styleRanges = Json.decodeFromString<List<StyleRange>>(textRecord.styleRanges)
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "[ ${getFormatedDate(timestamp = textRecord.createdAt)} ]", fontSize = 12.sp)
            Spacer(modifier = Modifier.size(8.dp))
            HorizontalDivider(modifier = Modifier.weight(1f), 2.dp)
            IconButton(onClick = { isDropDownClicked = !isDropDownClicked }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(dropDownRotation)
                )
            }
        }
        AnimatedVisibility(isDropDownClicked) {
            val context = LocalContext.current
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = buildAnnotatedStringHelper(textRecord.content, styleRanges))
                    Text(
                        text = "created at: ${
                            getFormatedDateWithHours(timestamp = textRecord.createdAt, context = context)
                        }", fontSize = 12.sp, modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun RecordPreview() {
    GameDiaryTheme {
        Record(TextRecord(1, 1, "", emptyList<StyleRange>().toString(), 123))
    }
}