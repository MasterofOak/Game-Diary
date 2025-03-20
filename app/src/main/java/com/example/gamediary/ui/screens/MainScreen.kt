package com.example.gamediary.ui.screens

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gamediary.R

enum class BottomNavigationItems(
    val selectedIcon: ImageVector, val unselectedIcon: ImageVector, @StringRes val iconName: Int, @StringRes val label:
    Int
) {
    
    GameButton(
        selectedIcon = Icons.Filled.VideogameAsset,
        unselectedIcon = Icons.Outlined.VideogameAsset,
        iconName = R.string.game_btn_icon_name,
        label = R.string.game_btn_label
    ),
    SearchButton(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconName = R.string.search_btn_icon_name,
        label = R.string.search_btn_label
    ),
    FeedButton(
        selectedIcon = Icons.Default.Newspaper,
        unselectedIcon = Icons.Outlined.Newspaper,
        iconName = R.string.feed_btn_icon_name,
        label = R.string.feed_btn_label
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameDiaryApp() {
    Scaffold(
        topBar = { TopNavBar(navigateUp = {}, canNavigateBack = true) },
        bottomBar = { BottomNavBar() },
        floatingActionButton = { ActionButton(onClick = {}) },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        GameScreen(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun ActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
    ) {
        Icon(imageVector = Icons.Default.Add, "Add Button")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(navigateUp: () -> Unit, canNavigateBack: Boolean) {
    CenterAlignedTopAppBar(
        title = { Text("Game Diary") },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = navigateUp,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
            
        },
        actions = {
            IconButton(
                onClick = navigateUp,
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options Button"
                )
            }
        },
        modifier = Modifier
    )
}

@Composable
fun BottomNavBar() {
    var selectedItem by remember { mutableIntStateOf(0) }
    NavigationBar(windowInsets = WindowInsets(bottom = 8.dp)) {
        BottomNavigationItems.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedItem == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = stringResource(item.iconName)
                    )
                },
                label = { Text(stringResource(item.label)) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
            
        }
    }
}

@Preview
@Composable
fun GameDiaryApp_Preview() {
    GameDiaryApp()
}