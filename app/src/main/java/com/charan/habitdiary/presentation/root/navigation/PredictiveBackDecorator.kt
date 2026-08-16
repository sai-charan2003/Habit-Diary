package com.charan.habitdiary.presentation.root.navigation

import androidx.compose.animation.EnterExitState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope

@Composable
fun <T : Any> rememberPredictiveBackCornerNavEntryDecorator(
    maxCornerRadius: Dp = 32.dp
): NavEntryDecorator<T> {
    return remember(maxCornerRadius) {
        NavEntryDecorator { entry ->
            val animatedScope = LocalNavAnimatedContentScope.current
            val isTransitioning = animatedScope?.let { scope ->
                scope.transition.currentState != EnterExitState.Visible ||
                    scope.transition.targetState != EnterExitState.Visible
            } ?: false

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        if (isTransitioning) {
                            shape = RoundedCornerShape(maxCornerRadius)
                            clip = true
                        }
                    }
            ) {
                entry.Content()
            }
        }
    }
}
