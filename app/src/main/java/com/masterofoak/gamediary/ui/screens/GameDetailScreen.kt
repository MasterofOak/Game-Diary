package com.masterofoak.gamediary.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.R
import com.masterofoak.gamediary.model.*
import com.masterofoak.gamediary.ui.theme.GameDiaryTheme
import com.masterofoak.gamediary.ui.viewmodel.VideoPlayerViewModel
import com.masterofoak.gamediary.utils.buildAnnotatedStringHelper
import com.masterofoak.gamediary.utils.getFormatedDate
import com.masterofoak.gamediary.utils.getFormatedDateWithHours
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDetailScreen(
    game: Game,
    getAllTextRecords: (Int) -> Flow<List<TextRecord>>,
    getAllImageRecords: (Int) -> Flow<List<ImageRecord>>,
    getAllVideoRecords: (Int) -> Flow<List<VideoRecord>>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    val textRecordsList by getAllTextRecords(game.id).collectAsState(initial = emptyList())
    val imageRecordsList by getAllImageRecords(game.id).collectAsState(initial = emptyList())
    val videoRecordsList by getAllVideoRecords(game.id).collectAsState(initial = emptyList())
    val recordsList = (textRecordsList + imageRecordsList + videoRecordsList)
        .groupBy {
            val formatedDate = getFormatedDate(it.createdAt)
            return@groupBy formatedDate
        }
        .toSortedMap().map { (date, items) -> date to items }
    val expandedDropDown: SnapshotStateMap<String, Boolean> = remember { mutableStateMapOf() }
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
        recordsList.forEach { (date, records) ->
            val isDropDownClicked = expandedDropDown.getOrElse(date) { false }
            stickyHeader(key = date) {
                val dropDownRotation by animateFloatAsState(
                    targetValue = if (isDropDownClicked) 180f else 0f, label = "DropDownArrowRotation"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "[ $date ]", fontSize = 12.sp)
                    Spacer(modifier = Modifier.size(8.dp))
                    HorizontalDivider(modifier = Modifier.weight(1f), 2.dp)
                    IconButton(onClick = { expandedDropDown[date] = !isDropDownClicked }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .rotate(dropDownRotation)
                        )
                    }
                }
            }
            item {
                AnimatedVisibility(
                    isDropDownClicked,
                    modifier = Modifier
                        .wrapContentSize()
                        .animateContentSize(),
                    enter = slideInVertically() + fadeIn(),
                    exit = shrinkOut() + fadeOut()
                ) {
                    Column {
                        records.forEach { record ->
                            when (record.recordType) {
                                RecordType.TEXT -> TextRecordComposable(record as TextRecord)
                                RecordType.IMAGE -> ImageRecordComposable(record as ImageRecord)
                                RecordType.VIDEO -> VideoRecordComposable(record as VideoRecord)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TextRecordComposable(textRecord: TextRecord, modifier: Modifier = Modifier) {
    val styleRanges = Json.decodeFromString<List<StyleRange>>(textRecord.styleRanges)
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

@Composable
private fun ImageRecordComposable(imageRecord: ImageRecord, modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val context = LocalContext.current
            AsyncImage(
                model = imageRecord.imageUri,
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier =
                    Modifier.aspectRatio(16f / 9f)
            )
            Text(
                text = "created at: ${
                    getFormatedDateWithHours(timestamp = imageRecord.createdAt, context = context)
                }", fontSize = 12.sp, modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoRecordComposable(videoRecord: VideoRecord, modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .aspectRatio(16f / 9f)
        ) {
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            
            // Initialize ExoPlayer and remember it across recompositions
            val videoPlayerViewModel: VideoPlayerViewModel = viewModel(
                key = "${videoRecord.videoUri}_${videoRecord.createdAt}"
            )
            
            DisposableEffect(videoRecord.videoUri) {
                videoPlayerViewModel.setVideoUrl(videoRecord.videoUri)
                onDispose { }
            }
            DisposableEffect(lifecycleOwner, videoPlayerViewModel.exoPlayer) {
                videoPlayerViewModel.setVideoUrl(videoRecord.videoUri)
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_STOP -> {
                            videoPlayerViewModel.exoPlayer.pause()
                        }
                        
                        Lifecycle.Event.ON_DESTROY -> {
                            videoPlayerViewModel.exoPlayer.release()
                        }
                        
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                
                // Clean up the observer when the composable leaves the composition
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = videoPlayerViewModel.exoPlayer
                        useController = true
                        controllerShowTimeoutMs = 2000
                        setShowPreviousButton(false)
                        setShowNextButton(false)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
            Text(
                text = "created at: " +
                        getFormatedDateWithHours(timestamp = videoRecord.createdAt, context = context),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
internal fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null, // to prevent the ripple from the tap
    ) {
        onClick()
    }

//@Composable
//private fun TextRecordComposable(textRecord: TextRecord, modifier: Modifier = Modifier) {
//    var isDropDownClicked by remember { mutableStateOf(false) }
//    val dropDownRotation by animateFloatAsState(
//        targetValue = if (isDropDownClicked) 180f else 0f, label = "DropDownArrowRotation"
//    )
//    val styleRanges = Json.decodeFromString<List<StyleRange>>(textRecord.styleRanges)
//    Column(modifier = modifier) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(text = "[ ${getFormatedDate(timestamp = textRecord.createdAt)} ]", fontSize = 12.sp)
//            Spacer(modifier = Modifier.size(8.dp))
//            HorizontalDivider(modifier = Modifier.weight(1f), 2.dp)
//            IconButton(onClick = { isDropDownClicked = !isDropDownClicked }) {
//                Icon(
//                    imageVector = Icons.Default.ArrowDropUp,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(32.dp)
//                        .rotate(dropDownRotation)
//                )
//            }
//        }
//        AnimatedVisibility(isDropDownClicked) {
//            val context = LocalContext.current
//            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
//                Column(modifier = Modifier.fillMaxWidth()) {
//                    Text(text = buildAnnotatedStringHelper(textRecord.content, styleRanges))
//                    Text(
//                        text = "created at: ${
//                            getFormatedDateWithHours(timestamp = textRecord.createdAt, context = context)
//                        }", fontSize = 12.sp, modifier = Modifier.align(Alignment.End)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun ImageRecordComposable(imageRecord: ImageRecord, modifier: Modifier = Modifier) {
//    var isDropDownClicked by remember { mutableStateOf(false) }
//    val dropDownRotation by animateFloatAsState(
//        targetValue = if (isDropDownClicked) 180f else 0f, label = "DropDownArrowRotation"
//    )
//    Column(modifier = modifier) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(text = "[ ${getFormatedDate(timestamp = imageRecord.createdAt)} ]", fontSize = 12.sp)
//            Spacer(modifier = Modifier.size(8.dp))
//            HorizontalDivider(modifier = Modifier.weight(1f), 2.dp)
//            IconButton(onClick = { isDropDownClicked = !isDropDownClicked }) {
//                Icon(
//                    imageVector = Icons.Default.ArrowDropUp,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(32.dp)
//                        .rotate(dropDownRotation)
//                )
//            }
//        }
//        AnimatedVisibility(isDropDownClicked) {
//            LocalContext.current
//            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
//                Column(modifier = Modifier.fillMaxWidth()) {
//                    val context = LocalContext.current
//                    AsyncImage(
//                        model = imageRecord.imageUri,
//                        contentScale = ContentScale.Crop,
//                        contentDescription = "",
//                        modifier =
//                            Modifier.aspectRatio(16f / 9f)
//                    )
//                    Text(
//                        text = "created at: ${
//                            getFormatedDateWithHours(timestamp = imageRecord.createdAt, context = context)
//                        }", fontSize = 12.sp, modifier = Modifier.align(Alignment.End)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
@Preview
fun RecordPreview() {
    GameDiaryTheme {
        TextRecordComposable(TextRecord(1, 1, "", emptyList<StyleRange>().toString(), 123))
    }
}