package com.masterofoak.gamediary.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.masterofoak.gamediary.R
import com.masterofoak.gamediary.ui.screens.add_game.Tag
import com.masterofoak.gamediary.ui.screens.game_details.ImageRecordComposable
import com.masterofoak.gamediary.ui.screens.game_details.TextRecordComposable
import com.masterofoak.gamediary.ui.screens.game_details.VideoRecordComposable
import com.masterofoak.gamediary.ui.viewmodel.SearchResult
import com.masterofoak.gamediary.ui.viewmodel.toFullTag

@Composable
fun SearchScreen(searchResults: List<SearchResult>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(searchResults) { result ->
            Surface(color = Color.DarkGray, shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(4.dp)) {
                when (result) {
                    is SearchResult.GameResult -> Row(
                        modifier = Modifier
                            .height(64.dp)
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = result.game.imageUri?.toUri(),
                            contentDescription = null,
                            error = painterResource(R.drawable.ai_slop_placeholder),
                            placeholder = painterResource(R.drawable.ai_slop_placeholder),
                            contentScale = ContentScale.Crop,
                            filterQuality = FilterQuality.Low,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(result.game.gameName)
                    }
                    
                    is SearchResult.TextRecordResult -> TextRecordComposable(
                        result.record, onEditRecord = {}, onDeleteRecord = {}
                    )
                    
                    is SearchResult.ImageRecordResult -> ImageRecordComposable(
                        result.record, onEditRecord = {}, onDeleteRecord = {}, onAddCaption = { int, str -> })
                    
                    is SearchResult.VideoRecordResult -> VideoRecordComposable(
                        result.record, onEditRecord = {}, onDeleteRecord = {}, onAddCaption = { int, str -> })
                    
                    is SearchResult.TagResult -> Column {
                        Tag(
                            result.tag.toFullTag(),
                            addTagToList = {},
                            isSelected = { false }
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 2.dp)
        }
    }
}