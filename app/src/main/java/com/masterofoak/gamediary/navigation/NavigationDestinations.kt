package com.masterofoak.gamediary.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector
import com.masterofoak.gamediary.R
import kotlinx.serialization.Serializable

object NavigationGraphs {
    @Serializable
    object GamesGraph
    
    @Serializable
    object SearchGraph
}

object NavigationDestinations {
    @Serializable
    data object GamesScreen
    
    @Serializable
    data class GameDetailScreen(val gameId: Int)
    
    @Serializable
    data class AddGameScreen(val gameId: Int?)
    
    @Serializable
    object SearchScreen
}

enum class BottomNavigationItems(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconName: Int,
    @StringRes val label: Int,
    val destination: Any
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
    )
}