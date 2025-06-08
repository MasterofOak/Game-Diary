package com.masterofoak.gamediary.ui.scaffold_base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    title: @Composable (() -> Unit) = {},
    isTopBarVisible: Boolean,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    extraAction: @Composable (RowScope.() -> Unit) = {}
) {
    AnimatedVisibility(isTopBarVisible, enter = slideInVertically(), exit = slideOutVertically()) {
        CenterAlignedTopAppBar(
            title = title,
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
            actions = extraAction,
            modifier = Modifier
        )
    }
}