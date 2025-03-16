package com.example.gamediary.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

enum class BottomNavigationItems(
    val selectedIcon: ImageVector, val unselectedIcon: ImageVector, val iconName: String, val label:
    String
) {
    
    HomeButton(
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconName = "Home Button",
        label = "Home"
    ),
    FeedButton(
        selectedIcon = Icons.Default.Done,
        unselectedIcon = Icons.Outlined.Done,
        iconName = "",
        label = "None"
    ),//TODO Future Feed/News Button
    AddButton(
        selectedIcon = Icons.Default.Add,
        unselectedIcon = Icons.Outlined.Add,
        iconName = "Add Button",
        label = "Add"
    ),
    PlaceholderButton(
        selectedIcon = Icons.Default.Done,
        unselectedIcon = Icons.Outlined.Done,
        iconName = "",
        label = "None"
    ), //TODO Not yet decided
    SettingsButton(
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        iconName = "Settings Button",
        label = "Settings"
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameDiaryApp() {
    Scaffold(
        topBar = { TopNavBar(navigateUp = {}, canNavigateBack = true) },
        bottomBar = { BottomNavBar() },
        modifier = Modifier.fillMaxSize()
    ) { _ ->
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
    NavigationBar {
        BottomNavigationItems.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(if (selectedItem == index) item.selectedIcon else item.unselectedIcon, item.iconName) },
                label = { Text(item.label) },
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