package com.example.gamediary.ui.screens

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideogameAsset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.gamediary.R
import com.example.gamediary.ui.navigation.NavigationDestinations
import com.example.gamediary.ui.navigation.NavigationGraphs

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

fun NavGraphBuilder.gamesGraph(navController: NavHostController) {
    navigation<NavigationGraphs.GamesGraph>(startDestination = NavigationDestinations.GamesScreen) {
        composable<NavigationDestinations.GamesScreen> { route ->
            GameScreen(modifier = Modifier, onGameClicked = {
                navController.navigate(NavigationDestinations.GameDetail) {
                }
            })
        }
        composable<NavigationDestinations.GameDetail> {
            GameDetail(modifier = Modifier)
        }
    }
}

fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    navigation<NavigationGraphs.SearchGraph>(startDestination = NavigationDestinations.SearchScreen) {
        composable<NavigationDestinations.SearchScreen> {
            SearchScreen(modifier = Modifier)
        }
    }
}

fun NavGraphBuilder.feedGraph(navController: NavHostController) {
    navigation<NavigationGraphs.FeedGraph>(startDestination = NavigationDestinations.FeedScreen) {
        composable<NavigationDestinations.FeedScreen> {
            FeedScreen(modifier = Modifier)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameDiaryApp(navController: NavHostController) {
    val entry by navController.currentBackStackEntryAsState()
    val currentDestination = entry?.destination
    Scaffold(
        topBar = {
            TopNavBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentDestination = currentDestination,
                navigateTo = { navigateTo(navController, it) }
            )
        },
        floatingActionButton = { ActionButton(onClick = {}) },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationGraphs.GamesGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            gamesGraph(navController)
            searchGraph(navController)
            feedGraph(navController)
        }
    }
}

@Composable
fun GameDetail(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text("Text")
    }
}

@Composable
fun ActionButton(onClick: () -> Unit) {
    var isFabClicked by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (isFabClicked) 45f else 0f, label = "FABRotation")
    LazyColumn(
        modifier = Modifier,
        horizontalAlignment = Alignment.End
    ) {
        items(3) {
            AnimatedVisibility(
                isFabClicked,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ExtendedFloatingActionButton(onClick = {}, modifier = Modifier.padding(4.dp)) {
                    Text("Something")
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(imageVector = Icons.Default.Add, "Add" + " Button")
                }
            }
        }
        item {
            FloatingActionButton(
                onClick = {
                    onClick(); isFabClicked = !isFabClicked
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Button",
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(canNavigateBack: Boolean, navigateUp: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Game Diary") },
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
fun BottomNavBar(currentDestination: NavDestination?, navigateTo: (destination: Any) -> Unit) {
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

private fun navigateTo(navController: NavHostController, destination: Any) {
    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true; inclusive = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Preview
@Composable
fun GameDiaryApp_Preview() {
    GameDiaryApp(rememberNavController())
}