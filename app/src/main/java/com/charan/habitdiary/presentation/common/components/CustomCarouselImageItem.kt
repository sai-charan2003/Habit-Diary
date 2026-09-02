package com.charan.habitdiary.presentation.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.video.VideoFrameDecoder
import com.charan.habitdiary.R
import com.charan.habitdiary.core.utils.isVideo
import com.charan.habitdiary.presentation.mediaviewer.components.MiniVideoPlayer
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun CustomCarouselImageItem(
    mediaPaths: List<String>,
    onRemoveClick: (String) -> Unit,
    isEdit: Boolean = false,
    modifier: Modifier = Modifier,
    onImageOpen: (String) -> Unit = {},
    overlayContent: @Composable (BoxScope.(index: Int) -> Unit)? = null,
) {
    if (mediaPaths.isEmpty()) return

    val imageLoader = rememberMediaImageLoader()
    val hazeState = rememberHazeState()
    var activeVideoPath by remember {
        mutableStateOf<String?>(null)
    }

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState {
            mediaPaths.size
        },
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        itemSpacing = 8.dp,
        preferredItemWidth = 200.dp,
    ) { index ->

        val item = mediaPaths[index]
        val isVideo = item.isVideo()
        val isPlaying = isVideo && activeVideoPath == item

        Card(
            modifier = Modifier
                .height(200.dp)
                .maskClip(MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().hazeBlur(
                        input = HazeInput.Sources(
                            state = hazeState,


                        ),
                        style = HazeBlurStyle {
                            noiseFactor(0f)
                            blurRadius(100.dp)
                            blurEnabled(true)
                        }
                    )
                )
                Crossfade(
                    targetState = isPlaying,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                    label = "video_transition"
                ) { playing ->

                    if (playing) {
                        MiniVideoPlayer(
                            videoPath = item,
                            onVideoClick = {
                                onImageOpen(item)
                            },
                            modifier = Modifier.hazeSource(hazeState).fillMaxSize()
                        )

                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            AsyncImage(
                                model = item,
                                imageLoader = imageLoader,
                                contentDescription = stringResource(
                                    R.string.media_preview
                                ),
                                modifier = Modifier
                                    .hazeSource(hazeState)
                                    .fillMaxSize()
                                    .clickable {
                                        onImageOpen(item)
                                    },
                                contentScale = ContentScale.Fit,
                            )

                            if (isVideo) {

                                FilledTonalIconButton(
                                    onClick = {
                                        activeVideoPath = item
                                    },
                                    shapes = IconButtonDefaults.shapes(),
                                    modifier = Modifier.align(Alignment.Center).size(IconButtonDefaults.extraSmallContainerSize()),
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                                    )
                                ) {

                                    Icon(
                                        imageVector = Icons.Rounded.PlayArrow,
                                        contentDescription = stringResource(R.string.play_or_pause_video)
                                    )
                                }
                            }
                        }
                    }
                }
                if (isEdit) {

                    FilledTonalIconButton(
                        onClick = {
                            onRemoveClick(item)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(26.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription =
                                stringResource(
                                    R.string.remove_button
                                ),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                overlayContent?.invoke(this, index)
            }
        }
    }
}

@Composable
private fun rememberMediaImageLoader(): ImageLoader {
    val context = LocalContext.current

    return remember {
        ImageLoader.Builder(context)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomCarouselImageItemPreview() {

    val sampleImages = listOf(
        "https://picsum.photos/200/300",
        "https://picsum.photos/300/300",
        "https://picsum.photos/400/300"
    )

    Box(
        modifier = Modifier.padding(
            vertical = 20.dp
        )
    ) {
        CustomCarouselImageItem(
            mediaPaths = sampleImages,
            onRemoveClick = {},
            isEdit = true
        )
    }
}