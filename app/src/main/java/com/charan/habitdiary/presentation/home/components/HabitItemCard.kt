package com.charan.habitdiary.presentation.home.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HabitItemCard(
    title: String,
    description: String,
    time: String = "",
    reminder: String = "",
    isCompleted: Boolean,
    onCompletedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    if (description.isNotBlank()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                RoundCheckbox(
                    checked = isCompleted,
                    onCheckedChange = onCompletedChange
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {

                if (time.isNotBlank()) {
                    HabitInfoItem(
                        icon = Icons.Rounded.Schedule,
                        text = time
                    )
                }

                if (reminder.isNotBlank()) {
                    HabitInfoItem(
                        icon = Icons.Rounded.Notifications,
                        text = reminder
                    )
                }
            }
        }
    }
}

@Composable
private fun HabitInfoItem(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 4.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}




@Preview(showBackground = true)
@Composable
fun HabitItemCardPreview() {
    MaterialTheme {
        HabitItemCard(
            title = "Morning Meditation",
            description = "10 minutes of guided mindfulness",
            time = "07:00 AM",
            reminder = "06:55 AM",
            isCompleted = false,
            onCompletedChange = {},
            onClick = {}
        )
    }
}

@Composable
fun RoundCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val strokeColor = MaterialTheme.colorScheme.outline
    val fillColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .clickable { onCheckedChange(!checked) }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {

            drawCircle(
                color = if (checked) fillColor.copy(alpha = 0.4f) else strokeColor,
                style = if (!checked) {
                    Stroke(width = 2.dp.toPx())
                } else {
                    Fill
                }
            )
            if (checked) {
                drawCircle(
                    color = fillColor,
                    radius = size.minDimension * 0.32f
                )
            }
        }
    }
}

