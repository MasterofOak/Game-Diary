package com.example.gamediary.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import com.example.gamediary.navigation.NavigationDestinations
import com.example.gamediary.navigation.NavigationGraphs
import com.example.gamediary.ui.scaffold_base.BottomNavBar
import com.example.gamediary.ui.scaffold_base.FloatingActionsButtonsList
import com.example.gamediary.ui.scaffold_base.ScaffoldBaseComposable
import com.example.gamediary.ui.scaffold_base.TopNavBar
import com.example.gamediary.ui.screens.*
import com.example.gamediary.ui.viewmodel.AddGameViewModel
import com.example.gamediary.ui.viewmodel.GamesViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDiaryApp(gameDiaryNavController: NavHostController) {
    val gamesViewModel: GamesViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    val addGameViewModel: AddGameViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    SharedTransitionLayout {
        NavHost(
            navController = gameDiaryNavController,
            startDestination = NavigationGraphs.GamesGraph,
            modifier = Modifier.fillMaxSize()
        )
        {
            gamesGraph(gameDiaryNavController, gamesViewModel, addGameViewModel, this@SharedTransitionLayout)
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
    sharedTransitionScope: SharedTransitionScope
) {
    navigation<NavigationGraphs.GamesGraph>(startDestination = NavigationDestinations.GamesScreen) {
        composable<NavigationDestinations.GamesScreen> { entry ->
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = "Games",
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) }
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
            ScaffoldBaseComposable(
                topBarComposable = {},
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) }
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
                GameDetailScreen(
                    viewModel = gamesViewModel,
                    sharedTransitionScope,
                    this@composable,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        composable<NavigationDestinations.AddGameScreen>(
            enterTransition = { scaleIn() },
            exitTransition = { scaleOut() },
            popEnterTransition = { scaleIn() },
            popExitTransition = { scaleOut() }
        ) {
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = "",
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
                    changeBottomNavBarVisibility = { },
                    navigateUp = { navController.navigateUp() },
                    modifier = Modifier.padding(innerPadding)
                )
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
                        title = "Search",
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) }
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
                        title = "Feed",
                        isTopBarVisible = true,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                floatingActionButtonComposable = {
                    FloatingActionsButtonsList(
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen) }
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
    GameDiaryApp(rememberNavController())
}