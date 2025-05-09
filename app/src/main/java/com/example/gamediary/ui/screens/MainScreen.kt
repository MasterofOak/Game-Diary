package com.example.gamediary.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gamediary.GlobalViewModelProvider
import com.example.gamediary.navigation.BottomNavigationItems
import com.example.gamediary.navigation.NavigationDestinations
import com.example.gamediary.navigation.NavigationGraphs
import com.example.gamediary.ui.viewmodel.AddGameViewModel
import com.example.gamediary.ui.viewmodel.GamesViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameDiaryApp(navController: NavHostController) {
    var topBarTitle by remember { mutableStateOf("") }
    var isTopBarVisible by remember { mutableStateOf(true) }
    var isBottomNavBarVisible by remember { mutableStateOf(true) }
    val entry by navController.currentBackStackEntryAsState()
    val currentDestination = entry?.destination
    Scaffold(
        topBar = {
            TopNavBar(
                title = topBarTitle,
                isTopBarVisible = isTopBarVisible,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentDestination = currentDestination,
                isBottomNavBarVisible = isBottomNavBarVisible,
                navigateTo = { navController.navigateBetweenBottomNavigation(it) }
            )
        },
        floatingActionButton = { ActionButtons(onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) }) },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val gamesViewModel: GamesViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
        val addGameViewModel: AddGameViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = NavigationGraphs.GamesGraph,
                modifier = Modifier.padding(innerPadding)
            ) {
                gamesGraph(
                    navController,
                    gamesViewModel,
                    addGameViewModel,
                    changeTopBarTitle = { topBarTitle = it },
                    changeTopBarVisibility = { isTopBarVisible = !isTopBarVisible },
                    changeBottomNavBarVisibility = { isBottomNavBarVisible = !isBottomNavBarVisible },
                    this@SharedTransitionLayout
                )
                searchGraph(navController)
                feedGraph(navController)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.gamesGraph(
    navController: NavHostController,
    gamesViewModel: GamesViewModel,
    addGameViewModel: AddGameViewModel,
    changeTopBarTitle: (String) -> Unit,
    changeTopBarVisibility: () -> Unit,
    changeBottomNavBarVisibility: () -> Unit,
    sharedTransitionScope: SharedTransitionScope
) {
    navigation<NavigationGraphs.GamesGraph>(startDestination = NavigationDestinations.GamesScreen) {
        composable<NavigationDestinations.GamesScreen> { entry ->
            changeTopBarTitle(entry.toRoute<NavigationDestinations.GamesScreen>().TITLE)
            GameScreen(
                viewModel = gamesViewModel,
                sharedTransitionScope,
                this@composable,
                onGameClicked = { navController.navigate(NavigationDestinations.GameDetailScreen(it)) },
                onAddGameClicked = { navController.navigate(NavigationDestinations.AddGameScreen) },
                modifier = Modifier
            
            )
        }
        composable<NavigationDestinations.GameDetailScreen> { entry ->
            val gameId = entry.toRoute<NavigationDestinations.GameDetailScreen>().gameId
            gamesViewModel.setGame(gameId)
            GameDetailScreen(
                viewModel = gamesViewModel,
                sharedTransitionScope,
                this@composable,
                modifier = Modifier
            )
        }
        composable<NavigationDestinations.AddGameScreen> {
            AddGameScreen(
                viewModel = addGameViewModel,
                changeBottomNavBarVisibility = changeBottomNavBarVisibility,
                navigateUp = { navController.navigateUp() }
            )
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

@Composable
fun ActionButtons(onGameFabClicked: () -> Unit) {
    var isFabClicked by remember { mutableStateOf(false) }
    val fabIconRotation by animateFloatAsState(targetValue = if (isFabClicked) 45f else 0f, label = "FABRotation")
    LazyColumn(
        modifier = Modifier,
        horizontalAlignment = Alignment.End
    ) {
        items(2) {
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
            AnimatedVisibility(
                isFabClicked,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { onGameFabClicked(); isFabClicked = !isFabClicked }, modifier = Modifier
                        .padding(4.dp)
                ) {
                    Text("Game")
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(imageVector = Icons.Default.VideogameAsset, "Add Game Action Button")
                }
            }
        }
        item {
            FloatingActionButton(
                onClick = {
                    isFabClicked = !isFabClicked
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Button",
                    modifier = Modifier.rotate(fabIconRotation)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(title: String, isTopBarVisible: Boolean, canNavigateBack: Boolean, navigateUp: () -> Unit) {
    AnimatedVisibility(isTopBarVisible, enter = slideInVertically(), exit = slideOutVertically()) {
        CenterAlignedTopAppBar(
            title = { Text(title) },
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
}

@Composable
fun BottomNavBar(
    currentDestination: NavDestination?,
    isBottomNavBarVisible: Boolean,
    navigateTo: (destination: Any) -> Unit
) {
    AnimatedVisibility(isBottomNavBarVisible, enter = slideInVertically(), exit = slideOutVertically()) {
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

private fun NavHostController.navigateBetweenBottomNavigation(destination: Any) {
    this.navigate(destination) {
        popUpTo(graph.findStartDestination().id) { saveState = true; inclusive = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Preview
@Composable
fun GameDiaryApp_Preview() {
    GameDiaryApp(rememberNavController())
}