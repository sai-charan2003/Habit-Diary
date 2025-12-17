package com.charan.habitdiary.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCarouselImageItem(
    mediaPaths: List<String>,
    onRemoveClick: (String) -> Unit,
    isEdit: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (mediaPaths.isEmpty()) return

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { mediaPaths.count() },
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        itemSpacing = 8.dp,
        preferredItemWidth = 200.dp,
    ) { index ->
        val item = mediaPaths[index]
        Card(
            modifier = Modifier
                .height(200.dp)
                .maskClip(MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = item,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(
                            radius = 25.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .alpha(0.6f),
                    contentScale = ContentScale.Crop
                )
                AsyncImage(
                    model = item,
                    contentDescription = "Log Entry Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                if (isEdit) {
                    FilledTonalIconButton(
                        onClick = { onRemoveClick(item) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(26.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Remove Image",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCarouselImageItemPreview() {
    val sampleImages = listOf(
        "https://picsum.photos/200/300",
        "https://picsum.photos/300/300",
        "https://picsum.photos/400/300"
    )

    // Preview Context
    Box(modifier = Modifier.padding(vertical = 20.dp)) {
        CustomCarouselImageItem(
            mediaPaths = sampleImages,
            onRemoveClick = {},
            isEdit = true
        )
    }
}