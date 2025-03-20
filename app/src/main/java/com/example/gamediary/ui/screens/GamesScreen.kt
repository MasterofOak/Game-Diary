package com.example.gamediary.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gamediary.R
import com.example.gamediary.ui.theme.GameDiaryTheme
import kotlin.math.absoluteValue

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
        pageSpacing = 16.dp,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
    ) { page ->
        val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue
        pagerState.currentPageOffsetFraction
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(pagerState.pageCount) {
                Card(modifier = Modifier.offset(y = (pageOffset * 40).dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.size(32.dp))
                        Text("Game #$page")
                    }
                }
            }
        }
        
    }
    
}

@Composable
@Preview
fun GameScreen_Preview() {
    GameDiaryTheme {
        GameScreen(modifier = Modifier.fillMaxSize())
    }
}