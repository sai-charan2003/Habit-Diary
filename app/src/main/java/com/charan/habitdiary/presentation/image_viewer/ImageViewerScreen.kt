package com.charan.habitdiary.presentation.image_viewer

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charan.habitdiary.presentation.common.components.BackButton
import com.charan.habitdiary.presentation.on_boarding.OnBoardingEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import kotlin.math.abs

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerScreen(
    allImages: List<String>,
    currentImage: String,
    onBack: () -> Unit
) {
    val pageState = rememberPagerState(pageCount = { allImages.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentImage) {
        pageState.scrollToPage(allImages.indexOf(currentImage))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { BackButton(onBackClick = onBack) }
            )
        }
    ) { padding ->
        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) { pageIndex ->
            val imageUrl = allImages[pageIndex]
            val offsetY = remember { Animatable(0f) }




            val configuration = LocalWindowInfo.current.containerSize
            val density = LocalDensity.current
            val dragLimit = with(density) { configuration.height.dp.toPx()} /11

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                val progress = abs(offsetY.value) / dragLimit
                                val resistance = (1f - progress).coerceIn(0.25f, 1f)
                                val resistedDrag = dragAmount * resistance

                                val newOffset = offsetY.value + resistedDrag
                                val clampedOffset = newOffset.coerceIn(-dragLimit, dragLimit)

                                scope.launch {
                                    offsetY.snapTo(clampedOffset)
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    if (abs(offsetY.value) > with(density) { 150.dp.toPx() }) {
                                        onBack()
                                    } else {
                                        offsetY.animateTo(
                                            0f,
                                            spring(stiffness = Spring.StiffnessMediumLow)
                                        )
                                    }
                                }
                            }
                        )
                    }
                    .graphicsLayer {
                        translationY = offsetY.value
                    }
            ) {
                ZoomableAsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
