package com.example.gamediary.ui.scaffold_base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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