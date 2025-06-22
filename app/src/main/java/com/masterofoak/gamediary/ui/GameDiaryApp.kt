package com.masterofoak.gamediary.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.masterofoak.gamediary.FILEPROVIDER_AUTHORITY
import com.masterofoak.gamediary.GlobalViewModelProvider
import com.masterofoak.gamediary.model.RecordType
import com.masterofoak.gamediary.navigation.NavigationDestinations
import com.masterofoak.gamediary.navigation.NavigationGraphs
import com.masterofoak.gamediary.ui.scaffold_base.*
import com.masterofoak.gamediary.ui.screens.add_game.AddGameScreen
import com.masterofoak.gamediary.ui.screens.game_details.GameDetailScreen
import com.masterofoak.gamediary.ui.screens.game_screen.GameScreen
import com.masterofoak.gamediary.ui.screens.search.SearchScreen
import com.masterofoak.gamediary.ui.secondary_composables.AddTagDialog
import com.masterofoak.gamediary.ui.secondary_composables.DeleteAlertDialog
import com.masterofoak.gamediary.ui.theme.GameDiaryTheme
import com.masterofoak.gamediary.ui.viewmodel.AddGameViewModel
import com.masterofoak.gamediary.ui.viewmodel.GamesViewModel
import com.masterofoak.gamediary.ui.viewmodel.SearchViewModel
import com.masterofoak.gamediary.ui.viewmodel.UserRecordViewModel
import com.masterofoak.gamediary.utils.deleteFileFromUri
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameDiaryApp(gameDiaryNavController: NavHostController) {
    val gamesViewModel: GamesViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    val addGameViewModel: AddGameViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    val userRecordViewModel: UserRecordViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
    val searchViewModel: SearchViewModel = viewModel(factory = GlobalViewModelProvider.Factory)
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
            searchGraph(
                gameDiaryNavController,
                searchViewModel
            
            )
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
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen(null)) },
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
            val textRecordsList by userRecordViewModel.getAllTextRecords(gameId).collectAsState(initial = emptyList())
            val imageRecordsList by userRecordViewModel.getAllImageRecords(gameId).collectAsState(initial = emptyList())
            val videoRecordsList by userRecordViewModel.getAllVideoRecords(gameId).collectAsState(initial = emptyList())
            var isDeleteDialogOpen by remember { mutableStateOf(false) }
            var isAllHeadersClosed by remember { mutableStateOf(false) }
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
                            val coroutineScope = rememberCoroutineScope()
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
                                        text = { Text("Close all headers") },
                                        onClick = {
                                            isAllHeadersClosed = !isAllHeadersClosed
                                            coroutineScope.launch {
                                                delay(500L)
                                                isAllHeadersClosed = !isAllHeadersClosed
                                            }
                                        },
                                        leadingIcon = { Icon(Icons.Outlined.ClearAll, contentDescription = null) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Edit game") },
                                        onClick = { navController.navigate(NavigationDestinations.AddGameScreen(gameId)) },
                                        leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete game") },
                                        onClick = { isDeleteDialogOpen = !isDeleteDialogOpen },
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
                        onGameFabClicked = { navController.navigate(NavigationDestinations.AddGameScreen(null)) },
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
                    isAllHeadersClosed = isAllHeadersClosed,
                    textRecordsList = textRecordsList,
                    imageRecordsList = imageRecordsList,
                    videoRecordsList = videoRecordsList,
                    onEditRecord = { record, type ->
                        GlobalDialogManager.showAlertDialog(
                            UserRecordDialogState(
                                userRecordViewModel = userRecordViewModel,
                                recordType = type,
                                recordToUpdate = record,
                                isUpdateMode = true,
                            )
                        )
                        userRecordViewModel.setSelectedGame(
                            gamesUiState.currentGame!!.id,
                            gamesUiState.currentGame!!.imageUri,
                            gamesUiState.currentGame!!.gameName
                        )
                    },
                    onDeleteRecord = userRecordViewModel::deleteRecord,
                    onAddCaptionToImage = userRecordViewModel::addCaptionToImage,
                    onAddCaptionToVideo = userRecordViewModel::addCaptionToVideo,
                    sharedTransitionScope,
                    this@composable,
                    modifier = Modifier.padding(innerPadding)
                )
                if (isDeleteDialogOpen) {
                    val context = LocalContext.current
                    DeleteAlertDialog(
                        titleText = "Are you sure?",
                        bodyText = "Deleting a game, will cause all data related to it be deleted!",
                        onDismissRequest = { isDeleteDialogOpen = false },
                        onConfirm = {
                            navController.navigateUp()
                            isDeleteDialogOpen = false
                            gamesViewModel.deleteGame(gameId)
                            for (record in imageRecordsList) {
                                val fileUri = record.imageUri.toUri()
                                val filesDir = context.filesDir
                                if (fileUri.authority == FILEPROVIDER_AUTHORITY) {
                                    deleteFileFromUri(fileUri, filesDir, "images/")
                                }
                            }
                            for (record in videoRecordsList) {
                                val fileUri = record.videoUri.toUri()
                                val filesDir = context.filesDir
                                if (fileUri.authority == FILEPROVIDER_AUTHORITY) {
                                    deleteFileFromUri(fileUri, filesDir, "videos/")
                                }
                            }
                        })
                }
            }
        }
        composable<NavigationDestinations.AddGameScreen>(
            enterTransition = { scaleIn() + fadeIn() },
            exitTransition = { scaleOut() + fadeOut() },
            popEnterTransition = { scaleIn() + fadeIn() },
            popExitTransition = { fadeOut() }
        ) { entry ->
            entry.toRoute<NavigationDestinations.AddGameScreen>().gameId
            val addGameUiState = addGameViewModel.uiState.collectAsState().value
            var isAddTagDialogOpen by remember { mutableStateOf(false) }
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
                                        onClick = { isAddTagDialogOpen = true },
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
                    addGameUiState = addGameUiState,
                    gameToUpdate = null,
                    addGame = addGameViewModel::addGame,
                    addSelectedTagToList = addGameViewModel::addSelectedTagToList,
                    navigateUp = { navController.navigateUp(); addGameViewModel.clearSelectedTags() },
                    modifier = Modifier.padding(innerPadding)
                )
                if (isAddTagDialogOpen) {
                    AddTagDialog(
                        onDialogDismiss = { isAddTagDialogOpen = false },
                        onConfirmButtonClicked =
                            { addGameViewModel.addCustomTag(it);isAddTagDialogOpen = false })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.searchGraph(navController: NavHostController, searchViewModel: SearchViewModel) {
    navigation<NavigationGraphs.SearchGraph>(startDestination = NavigationDestinations.SearchScreen) {
        composable<NavigationDestinations.SearchScreen> { entry ->
            val searchUiState = searchViewModel.uiState.collectAsState().value
            var searchQuery by remember { mutableStateOf("") }
            LaunchedEffect(searchQuery) {
                searchViewModel.getSearchResults("*${searchQuery}*")
            }
            ScaffoldBaseComposable(
                topBarComposable = {
                    TopNavBar(
                        title = {
                            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = {
                                        searchQuery = it
                                    },
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 16.sp),
                                    placeholder = { Text("Search") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    },
                                    trailingIcon = {
                                        if (searchUiState.searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { searchViewModel.updateSearchQuery("") }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Words,
                                        autoCorrectEnabled = true,
                                        keyboardType = KeyboardType.Text,
                                        showKeyboardOnFocus = true
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(CircleShape)
                                )
                            }
                        },
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
                val searchResults = searchUiState.searchResults
                SearchScreen(searchResults = searchResults, modifier = Modifier.padding(innerPadding))
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