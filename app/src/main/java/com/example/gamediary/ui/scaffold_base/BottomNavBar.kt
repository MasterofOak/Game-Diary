package com.example.gamediary.ui.scaffold_base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.gamediary.navigation.BottomNavigationItems

@Composable
fun BottomNavBar(
    currentDestination: NavDestination?,
    isBottomNavBarVisible: Boolean,
    navigateTo: (destination: Any) -> Unit
) {
    AnimatedVisibility(
        isBottomNavBarVisible,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        NavigationBar(windowInsets = WindowInsets(bottom = 8.dp)) {
            BottomNavigationItems.entries.forEach { item ->
                val isSelectedItem = currentDestination?.hierarchy?.any {
                    it.hasRoute(item.destination::class)
                } == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (isSelectedItem) item.selectedIcon else item.unselectedIcon,
                            contentDescription = stringResource(item.iconName)
                        )
                    },
                    label = { Text(stringResource(item.label)) },
                    selected = isSelectedItem,
                    onClick = { navigateTo(item.destination) }
                )
            }
        }
    }
}