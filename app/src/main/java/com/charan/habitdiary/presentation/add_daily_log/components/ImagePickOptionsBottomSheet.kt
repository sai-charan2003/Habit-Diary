package com.charan.habitdiary.presentation.add_daily_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charan.habitdiary.presentation.common.components.CustomListItem
import com.charan.habitdiary.ui.theme.indexItemFor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickOptionsBottomSheet(
    onImageFromGalleryClick : () -> Unit,
    onImageFromCameraClick : () -> Unit,
    onDismissRequest : () ->Unit,
    sheetState: SheetState
) {
    val imagePickOptions = listOf(
        ImagePickOptions(
            title = "Pick from Gallery",
            icon = Icons.Rounded.Image,
            onClick = onImageFromGalleryClick
        ),
        ImagePickOptions(
            title = "Take a Photo",
            icon = Icons.Rounded.Camera,
            onClick = onImageFromCameraClick
        )
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            imagePickOptions.forEachIndexed { index, option ->
                CustomListItem(
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = option.icon,
                                contentDescription = option.title,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(option.title)
                        }
                    },
                    indexItem = imagePickOptions.indexItemFor(index),
                    onClick = {
                        option.onClick()
                    },
                    contentPaddingValues = PaddingValues(20.dp)

                )
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ImagePickOptionsBottomSheetPreview() {
    ImagePickOptionsBottomSheet(
        onImageFromGalleryClick = {},
        onImageFromCameraClick = {},
        onDismissRequest = {},
        sheetState = rememberModalBottomSheetState()
    )
}

data class ImagePickOptions(
    val title : String,
    val icon : ImageVector,
    val onClick : () -> Unit
)