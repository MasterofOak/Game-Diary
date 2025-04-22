package com.example.gamediary.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gamediary.R
import kotlinx.serialization.Serializable

object NavigationGraphs {
    @Serializable
    object GamesGraph
    
    @Serializable
    object SearchGraph
    
    @Serializable
    object FeedGraph
}

object NavigationDestinations {
    @Serializable
    data object GamesScreen {
        
        const val TITLE = "Games"
    }
    
    @Serializable
    object GameDetailScreen
    
    @Serializable
    object AddGameScreen
    
    @Serializable
    object SearchScreen
    
    @Serializable
    object FeedScreen
}

enum class BottomNavigationItems(
    val selectedIcon: ImageVector, val unselectedIcon: ImageVector, @StringRes val iconName: Int, @StringRes val label:
    Int, val destination: Any
) {
    
    GameButton(
        selectedIcon = Icons.Filled.VideogameAsset,
        unselectedIcon = Icons.Outlined.VideogameAsset,
        iconName = R.string.game_btn_icon_name,
        label = R.string.game_btn_label,
        destination = NavigationGraphs.GamesGraph
    ),
    SearchButton(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconName = R.string.search_btn_icon_name,
        label = R.string.search_btn_label,
        destination = NavigationDestinations.SearchScreen
    ),
    FeedButton(
        selectedIcon = Icons.Default.Newspaper,
        unselectedIcon = Icons.Outlined.Newspaper,
        iconName = R.string.feed_btn_icon_name,
        label = R.string.feed_btn_label,
        destination = NavigationDestinations.FeedScreen
    )
}