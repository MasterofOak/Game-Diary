package com.masterofoak.gamediary.ui.screens.game_details

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.R
import com.masterofoak.gamediary.model.RecordType
import com.masterofoak.gamediary.model.Records
import com.masterofoak.gamediary.model.db_entities.Game
import com.masterofoak.gamediary.model.db_entities.ImageRecord
import com.masterofoak.gamediary.model.db_entities.TextRecord
import com.masterofoak.gamediary.model.db_entities.VideoRecord
import com.masterofoak.gamediary.ui.secondary_composables.DeleteAlertDialog
import com.masterofoak.gamediary.ui.theme.GameDiaryTheme
import com.masterofoak.gamediary.utils.getFormatedDate
import java.io.File


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun GameDetailScreen(
    game: Game,
    isAllHeadersClosed: Boolean,
    textRecordsList: List<TextRecord>,
    imageRecordsList: List<ImageRecord>,
    videoRecordsList: List<VideoRecord>,
    onEditRecord: (Records, RecordType) -> Unit,
    onDeleteRecord: (Records, RecordType, File) -> Unit,
    onAddCaptionToImage: (Int, String) -> Unit,
    onAddCaptionToVideo: (Int, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    val recordsList = (textRecordsList + imageRecordsList + videoRecordsList)
        .groupBy {
            val formatedDate = getFormatedDate(it.createdAt)
            return@groupBy formatedDate
        }
        .toSortedMap().map { (date, items) -> date to items }
    val expandedDropDownMap: SnapshotStateMap<String, Boolean> = remember { mutableStateMapOf() }
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
    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    var recordToDelete by remember { mutableStateOf<Records?>(null) }
    if (isDeleteDialogOpen) {
        val filesDir = LocalContext.current.filesDir
        DeleteAlertDialog(
            titleText = "Are you sure?",
            bodyText = "After deleting that record, data will be permanently erased",
            onDismissRequest = { isDeleteDialogOpen = false },
            onConfirm = {
                onDeleteRecord(
                    recordToDelete!!,
                    recordToDelete!!.recordType,
                    filesDir
                )
                isDeleteDialogOpen = false
                recordToDelete = null
            })
    }
    LaunchedEffect(isAllHeadersClosed == true) {
        expandedDropDownMap.forEach { key, _ ->
            expandedDropDownMap[key] = false
        }
    }
    LazyColumn(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        item {
            with(sharedTransitionScope) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .fillMaxWidth()
                        .size(currentImageSize)
                        .graphicsLayer {
                            scaleX = imageScale
                            scaleY = imageScale
                            // Center the image vertically as it scales
                            translationY = -(maxSize.toPx() - currentImageSize.toPx()) / 2f
                        }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(2f)
                            .border(2.dp, Color.White, RectangleShape)
                            .blur(32.dp)
                    ) {}
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
                            .clip(RoundedCornerShape(8.dp))
                            .zIndex(1f)
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
        }
        recordsList.forEach { (date, records) ->
            val isDropDownClicked = expandedDropDownMap.getOrElse(date) { false }
            stickyHeader(key = date) {
                DateStickyHeader(
                    date = date,
                    isDropDownClicked = isDropDownClicked,
                    expandedDropDownMap = expandedDropDownMap
                )
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
                                RecordType.TEXT -> TextRecordComposable(
                                    textRecord = record as TextRecord,
                                    onEditRecord = {
                                        onEditRecord(record, record.recordType)
                                    },
                                    onDeleteRecord = {
                                        isDeleteDialogOpen = !isDeleteDialogOpen; recordToDelete = record
                                    }
                                )
                                
                                RecordType.IMAGE -> ImageRecordComposable(
                                    imageRecord = record as ImageRecord,
                                    onEditRecord = {},
                                    onDeleteRecord = {
                                        isDeleteDialogOpen = !isDeleteDialogOpen; recordToDelete = record
                                    },
                                    onAddCaption = onAddCaptionToImage
                                )
                                
                                RecordType.VIDEO -> VideoRecordComposable(
                                    videoRecord = record as VideoRecord,
                                    onEditRecord = {},
                                    onDeleteRecord = {
                                        isDeleteDialogOpen = !isDeleteDialogOpen; recordToDelete = record
                                    },
                                    onAddCaption = onAddCaptionToVideo
                                )
                            }
                        }
                    }
                }
            }
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

@Composable
@Preview
fun RecordPreview() {
    GameDiaryTheme {
    }
}