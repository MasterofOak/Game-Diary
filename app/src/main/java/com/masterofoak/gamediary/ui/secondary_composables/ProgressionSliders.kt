package com.masterofoak.gamediary.ui.secondary_composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressionSliders(steps: Int, currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(steps) { i ->
            val animatedProgress by animateFloatAsState(
                targetValue = if (currentStep > i) 1f else 0f,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                drawStopIndicator = {},
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}