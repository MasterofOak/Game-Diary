package com.example.gamediary.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.gamediary.GlobalViewModelProvider
import com.example.gamediary.model.RecordType
import com.example.gamediary.navigation.NavigationDestinations
import com.example.gamediary.navigation.NavigationGraphs
import com.example.gamediary.ui.scaffold_base.BottomNavBar
import com.example.gamediary.ui.scaffold_base.FloatingActionsButtonsList
import com.example.gamediary.ui.scaffold_base.ScaffoldBaseComposable
import com.example.gamediary.ui.scaffold_base.TopNavBar
import com.example.gamediary.ui.screens.*
import com.example.gamediary.ui.secondary_composables.GamesDropDown
import com.example.gamediary.ui.theme.GameDiaryTheme
import com.example.gamediary.ui.viewmodel.AddGameViewModel
import com.example.gamediary.ui.viewmodel.GamesViewModel
import com.example.gamediary.ui.viewmodel.UserRecordViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDiaryApp(gameDiaryNavController: NavHostController) {
    val gamesViewModel: GamesViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    val addGameViewModel: AddGameViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    val userRecordViewModel: UserRecordViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    SharedTransitionLayout {
        NavHost(
            navController = gameDiaryNavController,
            startDestination = NavigationGraphs.GamesGraph,
            modifier = Modifier.fillMaxSize()
        )
        {
            gamesGraph(
                gameDiaryNavController,
                gamesViewModel,
                addGameViewModel,
                userRecordViewModel,
                this@SharedTransitionLayout
            )
            searchGraph(gameDiaryNavController)
            feedGraph(gameDiaryNavController)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.gamesGraph(
    navController: NavHostController,
    gamesViewModel: GamesViewModel,
    addGameViewModel: AddGameViewModel,
    userRecordViewModel: UserRecordViewModel,
    sharedTransitionScope: SharedTransitionScope
) {
    navigation<NavigationGraphs.GamesGraph>(startDestination = NavigationDestinations.GamesScreen) {
        composable<NavigationDestinations.GamesScreen>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { entry ->
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = { Text("Games") },
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) },
                        onTextRecordFabClicked = {
                            navController.navigate(
                                NavigationDestinations.AddUserRecordScreen(
                                    RecordType.TEXT
                                )
                            )
                        }
                    )
                },
                bottomBarComposable = {
                    BottomNavBar(
                        currentDestination = entry.destination,
                        isBottomNavBarVisible = true,
                        navigateTo = { navController.navigateBetweenBottomNavigation(it) }
                    )
                },
            ) { innerPadding ->
                GameScreen(
                    viewModel = gamesViewModel,
                    sharedTransitionScope,
                    this@composable,
                    onGameClicked = { navController.navigate(NavigationDestinations.GameDetailScreen(it)) },
                    onAddGameClicked = { navController.navigate(NavigationDestinations.AddGameScreen) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            
        }
        composable<NavigationDestinations.GameDetailScreen> { entry ->
            val gameId = entry.toRoute<NavigationDestinations.GameDetailScreen>().gameId
            gamesViewModel.setGame(gameId)
            val gamesUiState = gamesViewModel.uiState.collectAsState().value
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = {
                            with(sharedTransitionScope) {
                                Text(
                                    gamesUiState.currentGame!!.gameName,
                                    modifier = Modifier.sharedBounds(
                                        sharedTransitionScope.rememberSharedContentState(
                                            key = "text-${gamesUiState.currentGame!!.id}"
                                        ),
                                        this@composable
                                    )
                                )
                            }
                        },
                        extraAction = {
                            var isMoreOptionClicked by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { isMoreOptionClicked = !isMoreOptionClicked }) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "More option"
                                    )
                                }
                                DropdownMenu(
                                    expanded = isMoreOptionClicked,
                                    onDismissRequest = { isMoreOptionClicked = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Edit game") },
                                        onClick = { /* Handle edit! */ },
                                        leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete game") },
                                        onClick = { /* Handle deletion! */ },
                                        leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) }
                                    )
                                }
                            }
                        },
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) },
                        onTextRecordFabClicked = {
                            navController.navigate(
                                NavigationDestinations.AddUserRecordScreen
                                    (RecordType.TEXT)
                            )
                        }
                    )
                },
                bottomBarComposable = {},
                modifier = Modifier
            ) { innerPadding ->
                GameDetailScreen(
                    game = gamesUiState.currentGame!!,
                    getAllTextRecords = userRecordViewModel::getAllTextRecords,
                    sharedTransitionScope,
                    this@composable,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        composable<NavigationDestinations.AddGameScreen>(
            enterTransition = { scaleIn() + fadeIn() },
            exitTransition = { scaleOut() + fadeOut() },
            popEnterTransition = { scaleIn() + fadeIn() },
            popExitTransition = { fadeOut() }
        ) {
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = { Text("") },
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {},
                bottomBarComposable = {}
            ) { innerPadding ->
                AddGameScreen(
                    viewModel = addGameViewModel,
                    navigateUp = { navController.navigateUp() },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        composable<NavigationDestinations.AddUserRecordScreen>(
            enterTransition = { scaleIn() },
            exitTransition = { scaleOut() },
            popEnterTransition = { scaleIn() },
            popExitTransition = { scaleOut() },
        ) { entry ->
            val userRecordUiState = userRecordViewModel.uiState.collectAsState().value
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = {
                            GamesDropDown(
                                userRecordUiState,
                                setGame = { gameId, gameImage ->
                                    userRecordViewModel.setCurrentGame(gameId, gameImage)
                                },
                                getAllGames = userRecordViewModel::getAllGamesList
                            )
                        },
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        extraAction = {
                            var isMoreOptionClicked by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { isMoreOptionClicked = !isMoreOptionClicked }) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "More option"
                                    )
                                }
                                DropdownMenu(
                                    expanded = isMoreOptionClicked,
                                    onDismissRequest = { isMoreOptionClicked = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Edit game") },
                                        onClick = { /* Handle edit! */ },
                                        leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete game") },
                                        onClick = { /* Handle deletion! */ },
                                        leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) }
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButtonComposable = {},
                bottomBarComposable = {}
            ) { innerPadding ->
                val recordType = entry.toRoute<NavigationDestinations.AddUserRecordScreen>().recordType
                when (recordType) {
                    RecordType.TEXT -> AddTextRecordScreen(
                        addTextRecord = { userRecordViewModel.addTextRecords(it) },
                        userRecordUiState = userRecordUiState,
                        modifier = Modifier.padding(innerPadding)
                    )
                    
                    RecordType.IMAGE -> AddImageRecordScreen(
                        addImageRecord = { userRecordViewModel.addImageRecords(it) },
                        userRecordUiState = userRecordUiState,
                        modifier = Modifier.padding(innerPadding)
                    )
                    
                    RecordType.VIDEO -> AddVideoRecordScreen(
                        addVideoRecord = { userRecordViewModel.addVideoRecords(it) },
                        userRecordUiState = userRecordUiState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    navigation<NavigationGraphs.SearchGraph>(startDestination = NavigationDestinations.SearchScreen) {
        composable<NavigationDestinations.SearchScreen> { entry ->
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = { Text("Search") },
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) },
                        onTextRecordFabClicked = {
                            navController.navigate(
                                NavigationDestinations.AddUserRecordScreen(
                                    RecordType.TEXT
                                )
                            )
                        }
                    )
                },
                bottomBarComposable = {
                    BottomNavBar(
                        currentDestination = entry.destination,
                        isBottomNavBarVisible = true,
                        navigateTo = { navController.navigateBetweenBottomNavigation(it) }
                    )
                }
            ) { innerPadding ->
                SearchScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

fun NavGraphBuilder.feedGraph(navController: NavHostController) {
    navigation<NavigationGraphs.FeedGraph>(startDestination = NavigationDestinations.FeedScreen) {
        composable<NavigationDestinations.FeedScreen> { entry ->
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = { Text("Feed") },
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) },
                        onTextRecordFabClicked = {
                            navController.navigate(
                                NavigationDestinations.AddUserRecordScreen(
                                    RecordType.TEXT
                                )
                            )
                        }
                    )
                },
                bottomBarComposable = {
                    BottomNavBar(
                        currentDestination = entry.destination,
                        isBottomNavBarVisible = true,
                        navigateTo = { navController.navigateBetweenBottomNavigation(it) },
                    )
                }
            ) { innerPadding ->
                FeedScreen(modifier = Modifier.padding(innerPadding))
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
    GameDiaryTheme {
        GameDiaryApp(rememberNavController())
    }
}