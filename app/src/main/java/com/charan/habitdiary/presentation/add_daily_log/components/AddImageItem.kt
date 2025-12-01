package com.charan.habitdiary.presentation.add_daily_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddImageItem(
    imageURL: String,
    onClick : () -> Unit = {}
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .padding(vertical = 20.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            if (imageURL.isNotEmpty()) {
                AsyncImage(
                    model = imageURL,
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Rounded.AddAPhoto,
                    contentDescription = "Add Image",
                    tint = if (imageURL.isEmpty()) MaterialTheme.colorScheme.onSurface else Color.White.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Tap to add image",
                    style = MaterialTheme.typography.bodyMediumEmphasized,
                    color = if (imageURL.isEmpty()) MaterialTheme.colorScheme.onSurface else Color.White
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Capture your moments",
                    style = MaterialTheme.typography.labelMediumEmphasized.copy(
                        fontWeight = FontWeight.Light
                    ),
                    color = if (imageURL.isEmpty()) MaterialTheme.colorScheme.onSurface else Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}


@Preview
@Composable
fun AddImageItemPreview() {
    AddImageItem(
        imageURL = ""
    )
}