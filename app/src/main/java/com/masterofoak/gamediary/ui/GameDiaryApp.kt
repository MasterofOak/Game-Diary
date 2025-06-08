package com.masterofoak.gamediary.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddCircle
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
import com.masterofoak.gamediary.GlobalViewModelProvider
import com.masterofoak.gamediary.model.RecordType
import com.masterofoak.gamediary.navigation.NavigationDestinations
import com.masterofoak.gamediary.navigation.NavigationGraphs
import com.masterofoak.gamediary.ui.scaffold_base.*
import com.masterofoak.gamediary.ui.screens.*
import com.masterofoak.gamediary.ui.secondary_composables.AddTagDialog
import com.masterofoak.gamediary.ui.theme.GameDiaryTheme
import com.masterofoak.gamediary.ui.viewmodel.AddGameViewModel
import com.masterofoak.gamediary.ui.viewmodel.GamesViewModel
import com.masterofoak.gamediary.ui.viewmodel.UserRecordViewModel

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
                        canNavigateBack = false,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) },
                        onTextRecordFabClicked = {
                            GlobalDialogManager.showAlertDialog(
                                UserRecordDialogState(
                                    userRecordViewModel = userRecordViewModel,
                                    recordType = RecordType.TEXT,
                                )
                            )
                        },
                        onImageRecordFabClicked = {
                            GlobalDialogManager.showAlertDialog(
                                UserRecordDialogState(
                                    userRecordViewModel = userRecordViewModel,
                                    recordType = RecordType.IMAGE,
                                )
                            )
                        },
                        onVideoRecordFabClicked = {
                            GlobalDialogManager.showAlertDialog(
                                UserRecordDialogState(
                                    userRecordViewModel = userRecordViewModel,
                                    recordType = RecordType.VIDEO,
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
                            GlobalDialogManager.showAlertDialog(
                                UserRecordDialogState(
                                    userRecordViewModel = userRecordViewModel,
                                    recordType = RecordType.TEXT,
                                )
                            )
                        },
                        onImageRecordFabClicked = {
                            GlobalDialogManager.showAlertDialog(
                                UserRecordDialogState(
                                    userRecordViewModel = userRecordViewModel,
                                    recordType = RecordType.IMAGE,
                                )
                            )
                        },
                        onVideoRecordFabClicked = {
                            GlobalDialogManager.showAlertDialog(
                                UserRecordDialogState(
                                    userRecordViewModel = userRecordViewModel,
                                    recordType = RecordType.VIDEO,
                                )
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
                    getAllImageRecords = userRecordViewModel::getAllImageRecords,
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
            var isDialogOpen by remember { mutableStateOf(false) }
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = { Text("") },
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp(); addGameViewModel.clearSelectedTags() },
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
                                        text = { Text("Add Custom Tag") },
                                        onClick = { isDialogOpen = true },
                                        leadingIcon = { Icon(Icons.Outlined.AddCircle, contentDescription = null) }
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButtonComposable = {},
                bottomBarComposable = {}
            ) { innerPadding ->
                AddGameScreen(
                    viewModel = addGameViewModel,
                    navigateUp = { navController.navigateUp(); addGameViewModel.clearSelectedTags() },
                    modifier = Modifier.padding(innerPadding)
                )
                if (isDialogOpen) {
                    AddTagDialog(
                        onDialogDismiss = { isDialogOpen = false },
                        onConfirmButtonClicked =
                            { addGameViewModel.addCustomTag(it);isDialogOpen = false })
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
                        canNavigateBack = false,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
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
                        canNavigateBack = false,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
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