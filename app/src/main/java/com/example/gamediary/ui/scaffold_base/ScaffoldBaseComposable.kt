package com.example.gamediary.ui.scaffold_base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScaffoldBaseComposable(
    topBarComposable: @Composable () -> Unit,
    floatingActionButtonComposable: @Composable () -> Unit,
    bottomBarComposable: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    mainContentComposable: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBarComposable,
        floatingActionButton = floatingActionButtonComposable,
        bottomBar = bottomBarComposable,
        modifier = modifier
    ) { innerPadding ->
        mainContentComposable(innerPadding)
    }
}